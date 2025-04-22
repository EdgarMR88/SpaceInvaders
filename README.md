
# 👾 Space Invaders

Proyecto clásico de Space Invaders programado en Java.

## 🚀 Descripción

Este juego recrea el legendario **Space Invaders** usando Java puro. Puedes moverte con las teclas y disparar para eliminar enemigos alienígenas. Fue creado como práctica del módulo de **Entornos de Desarrollo** en el **grado superior de Desarrollo de Aplicaciones Web**.

## 🧱 Estructura del Proyecto

```
📁 Space Invaders
 ├── build.bat              ← Script para compilar y empaquetar (opcional)
 ├── MANIFEST.MF            ← Archivo de manifiesto para el .jar
 ├── *.java                 ← Clases Java del juego
 ├── bin/                   ← Clases compiladas
 ├── SpaceInvaders.jar      ← Ejecutable Java (auto-generado)
 ├── SpaceInvaders.exe      ← Ejecutable Windows (opcional)
 ├── icon.ico               ← Icono personalizado estilo retro
 ├── *.png                  ← Imágenes (por ejemplo, player.png, alien.png)
 ├── *.wav                  ← Sonidos (por ejemplo, music.wav, shoot.wav)
 └── README.md              ← Este archivo
```

## 🔧 Requisitos

- **Java JDK 8 o superior**: Asegúrate de tener Java instalado. Puedes descargarlo desde [la página oficial de Java](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html).
- **Opcional**: [Launch4j](http://launch4j.sourceforge.net/) para generar el `.exe`.
- **Opcional**: [Inno Setup](https://jrsoftware.org/isinfo.php) para crear instaladores de Windows.

## ⚙️ Cómo compilar y ejecutar

### 💡 Opción 1: Usar el script automático

1. Haz doble clic en `build.bat` para compilar automáticamente el código y generar el archivo `SpaceInvaders.jar`.
2. Ejecuta el juego con el siguiente comando:

   ```bash
   java -jar SpaceInvaders.jar
   ```

### 💡 Opción 2: Manual

Si prefieres compilar el juego manualmente, puedes usar los siguientes comandos en la terminal:

1. Compila el código fuente:

   ```bash
   javac -d bin *.java
   ```

2. Crea el archivo JAR:

   ```bash
   jar cfm SpaceInvaders.jar MANIFEST.MF -C bin .
   ```

3. Ejecuta el archivo JAR:

   ```bash
   java -jar SpaceInvaders.jar
   ```

## 🖼️ Icono personalizado

El juego utiliza un icono `.ico` estilo retro (tipo Space Invaders) para el ejecutable de Windows.

## 💾 Versión instalable

Puedes usar **Inno Setup** para crear un instalador clásico que incluya:

- Un acceso directo en el escritorio.
- Una carpeta en Archivos de Programa.
- Un desinstalador automático.

## 🤝 Contribuciones

Si deseas contribuir a este proyecto, sigue estos pasos:

1. Haz un **fork** de este repositorio.
2. Crea una rama para tu nueva característica (`git checkout -b feature/nueva-caracteristica`).
3. Haz commit de tus cambios (`git commit -am 'Añadir nueva característica'`).
4. Haz push a la rama (`git push origin feature/nueva-caracteristica`).
5. Abre un **Pull Request**.

## 📥 Compartir e Instalar

Para compartir el juego con tus amigos o familiares, puedes enviarles el archivo **`SpaceInvaders_Installer.exe`** por correo electrónico o copiarlo en un USB.

### Instrucciones para instalar el juego:

1. **Ejecuta el instalador**: Haz doble clic en **`SpaceInvaders_Installer.exe`**.
2. **Sigue los pasos del instalador**: El instalador te guiará a través del proceso de instalación.
3. **Accede al juego**: Una vez instalado, puedes jugar al juego desde el acceso directo en el **escritorio** o en el **menú de inicio** (según cómo lo hayas configurado en el instalador).

## 📝 Licencia

Este proyecto está bajo la **licencia MIT**. Puedes usar, copiar, modificar y distribuir el código de este proyecto bajo los términos de la licencia MIT. Para más detalles, consulta el archivo [LICENSE](LICENSE) que se encuentra en este repositorio.

## ✍️ Autor

- **Edgar (2025)**  
- Estudiante de Desarrollo de Aplicaciones Web  
- Amante de los videojuegos, la programación y el humor en redes sociales.

---

¡Gracias por jugar! 🚀
