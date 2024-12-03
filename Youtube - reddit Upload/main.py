import praw
from RedDownloader import RedDownloader
import os
import time
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from googleapiclient.http import MediaFileUpload
from google.auth.transport.requests import Request
from datetime import datetime, timedelta
import goslate
from translate import Translator
import json

# Cargar configuraciones sensibles desde config.json
with open("config.json", "r") as config_file:
    config = json.load(config_file)

# Configuración de la API de Reddit
reddit = praw.Reddit(
    user_agent=config["reddit"]["user_agent"],
    client_id=config["reddit"]["client_id"],
    client_secret=config["reddit"]["client_secret"],
    username=config["reddit"]["username"],
    password=config["reddit"]["password"]
)

# Especifica el subreddit que quieres monitorear
subreddit = reddit.subreddit("BeAmazed")

# Configuración de la API de YouTube
CLIENT_SECRETS_FILE = config["youtube"]["client_secrets_file"]
TOKEN_FILE = config["youtube"]["token_file"]
SCOPES = ['https://www.googleapis.com/auth/youtube.upload']

def get_authenticated_service():
    creds = None
    # Verificar si el archivo de token ya existe
    if os.path.exists(TOKEN_FILE):
        creds = Credentials.from_authorized_user_file(TOKEN_FILE, SCOPES)
    # Si no hay credenciales válidas, se inicia el flujo de autenticación
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())  # Pass Request() as an argument to refresh
        else:
            flow = InstalledAppFlow.from_client_secrets_file(CLIENT_SECRETS_FILE, SCOPES)
            creds = flow.run_local_server(port=0)
        # Guardar las credenciales en el archivo para futuras ejecuciones
        with open(TOKEN_FILE, 'w') as token:
            token.write(creds.to_json())
    return build('youtube', 'v3', credentials=creds)

youtube = get_authenticated_service()

# Función para limpiar y truncar el título
def clean_and_truncate_title(title):
    if not title:
        return "Video sin título"
    title = title[:100]  # Limitar la longitud del título a 100 caracteres
    invalid_chars = ['<', '>', ':', '"', '/', '\\', '|', '?', '*']
    for char in invalid_chars:
        title = title.replace(char, '')
    return title

# Función para programar la subida de un video a YouTube
def schedule_video_upload(video_path, title, description, publish_at):
    title = clean_and_truncate_title(title)
    body = {
        'snippet': {
            'title': title,
            'description': description if description else "",
            'tags': ['meme'],
            'categoryId': '22'
        },
        'status': {
            'privacyStatus': 'private',  # Se sube como privado hasta que sea publicado
            'embeddable': True,
            'license': 'youtube',
            'publishAt': publish_at.isoformat() + 'Z'  # Tiempo en formato ISO para la API
        }
    }
    media = MediaFileUpload(video_path, chunksize=-1, resumable=True)
    request = youtube.videos().insert(
        part="snippet,status",
        body=body,
        media_body=media
    )
    response = None
    while response is None:
        status, response = request.next_chunk()
        if status:
            print(f"Subiendo video a YouTube: {int(status.progress() * 100)}% completado")
    print(f"Video programado para publicación: {title} a las {publish_at}")

# Función para descargar y programar un video
def process_video(submission, publish_at):
    if submission.is_video:
        video_url = submission.url
        video_file = f"{submission.id}.mp4"
        
        try:
            # Descargar video con audio usando RedDownloader
            print(f"Descargando video: {submission.title}")
            file = RedDownloader.Download(url=video_url, output=submission.id, quality=720)
            print(f"Video descargado: {submission.title} ")
        except Exception as e:
            print(f"Error al descargar {submission.title}: {str(e)}")
            return  # Salir de la función en caso de error

        # Programar la subida del video a YouTube
        try:
            traductor = Translator(to_lang='es')
            spanishTitle = traductor.translate(submission.title)
            print(spanishTitle)
            print(f"Programando video: {submission.title} para ser publicado a las {publish_at}")
            schedule_video_upload(video_file, spanishTitle, submission.selftext, publish_at)
        except Exception as e:
            print(f"Error al subir {submission.title} a YouTube: {str(e)}")
            return

        # Eliminar el video descargado
        if os.path.exists(video_file):
            os.remove(video_file)
            print(f"{video_file} eliminado correctamente.")

# Variables para manejar los videos pendientes
videos_pendientes = []

# Función para obtener los videos de Reddit
def obtener_videos():
    global videos_pendientes
    for submission in subreddit.new(limit=9):  # Cambia el límite según sea necesario
        if submission.is_video:
            videos_pendientes.append(submission)
    print(f"{len(videos_pendientes)} videos obtenidos de Reddit.")

# Función para programar las subidas de los videos
def programar_subidas():
    publish_at = datetime.utcnow()  # Hora actual en UTC
    for video in videos_pendientes:
        process_video(video, publish_at)
        publish_at += timedelta(hours=1)  # Programar cada video 1 hora después del anterior
        

# Obtener los videos y programar sus subidas
obtener_videos()
programar_subidas()
