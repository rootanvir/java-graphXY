import sys
from PyQt5.QtWidgets import QApplication, QWidget, QLabel, QDesktopWidget
from PyQt5.QtGui import QPixmap
from PyQt5.QtCore import Qt

class TransparentWindow(QWidget):
    def __init__(self):
        super().__init__()

        self.setWindowTitle("Transparent Click-through Fullscreen Window")

        # Get screen geometry
        screen = QDesktopWidget().screenGeometry()
        self.setGeometry(screen)

        # Set window transparency
        self.setWindowOpacity(0.5)  # Set opacity to 50%

        # Set window flag to make it click-through and always on top
        self.setWindowFlags(Qt.Window | Qt.WindowTransparentForInput | Qt.WindowStaysOnTopHint)

        # Create a label to display the image
        self.label = QLabel(self)
        pixmap = QPixmap("img.png")  # Change "image.jpg" to the path of your image file
        self.label.setPixmap(pixmap)
        self.label.setAlignment(Qt.AlignCenter)

    def keyPressEvent(self, event):
        if event.key() == Qt.Key_Z and event.modifiers() & Qt.ControlModifier:
            sys.exit()  # Exit the application
        else:
            super().keyPressEvent(event)

if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = TransparentWindow()
    window.showFullScreen()
    sys.exit(app.exec_())
