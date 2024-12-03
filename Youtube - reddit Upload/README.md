Automatización de Subida de Videos de Reddit a YouTube

Descripción:
Este proyecto automatiza el proceso de obtener videos de un subreddit específico, traducir su título al español y programar su subida a YouTube. Utiliza las APIs de Reddit y YouTube para integrar ambas plataformas, lo que permite gestionar los videos de manera eficiente, asegurando que se publiquen en horarios programados.

Características principales:

Monitoreo de Reddit:
Utiliza la API de Reddit para buscar los videos más recientes de un subreddit.

Descarga de videos:
Descarga los videos de Reddit con la herramienta RedDownloader.

Traducción automática:
Traduce los títulos de los videos al español utilizando translate.

Subida programada a YouTube:
Sube los videos a YouTube como privados y los programa para publicarlos en horarios específicos.

Gestión de credenciales:
Utiliza OAuth 2.0 para la autenticación con la API de YouTube.

Limpieza de archivos:
Los archivos descargados se eliminan después de ser procesados para mantener el entorno limpio.

Tecnologías usadas:

Python
APIs de Reddit (PRAW) y YouTube (Google API Client)
RedDownloader para descargar videos
Traducción automática con translate
Instrucciones de uso:

Configura las credenciales en un archivo config.json.
Instala las dependencias necesarias usando pip install -r requirements.txt.
Ejecuta el script para monitorear el subreddit y programar las subidas.
Requisitos:

Cuenta de Reddit con credenciales de API.
Acceso a la API de YouTube habilitada con claves y tokens.
Este proyecto puede ser extendido para manejar otros formatos de contenido, mejorar el manejo de errores o agregar análisis de datos sobre los videos subidos.