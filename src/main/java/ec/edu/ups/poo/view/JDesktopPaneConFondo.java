package ec.edu.ups.poo.view;

import javax.swing.*;
import java.awt.*;

public class JDesktopPaneConFondo extends JDesktopPane {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo oscuro (sala de control)
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, w, h);

        // Piso reflectante
        GradientPaint piso = new GradientPaint(0, h / 2, new Color(40, 40, 50),
                0, h, new Color(20, 20, 20));
        g2.setPaint(piso);
        g2.fillRect(0, h / 2, w, h / 2);

        // Racks de servidores (izquierda y derecha)
        for (int i = 0; i < 4; i++) {
            int x = 40 + i * 70;
            int y = h / 2 - 150;
            g2.setColor(new Color(30, 30, 30));
            g2.fillRoundRect(x, y, 50, 120, 10, 10);
            g2.setColor(Color.GREEN);
            for (int j = 0; j < 4; j++) {
                g2.fillRect(x + 10, y + 10 + j * 25, 30, 10);
            }
        }

        for (int i = 0; i < 4; i++) {
            int x = w - 320 + i * 70;
            int y = h / 2 - 150;
            g2.setColor(new Color(30, 30, 30));
            g2.fillRoundRect(x, y, 50, 120, 10, 10);
            g2.setColor(Color.GREEN);
            for (int j = 0; j < 4; j++) {
                g2.fillRect(x + 10, y + 10 + j * 25, 30, 10);
            }
        }

        // Consola central con 3 pantallas
        int cx = w / 2 - 150;
        int cy = h / 2 - 120;
        g2.setColor(new Color(50, 50, 60));
        g2.fillRoundRect(cx, cy, 300, 150, 20, 20);

        // Pantallas
        g2.setColor(Color.BLACK);
        g2.fillRect(cx + 10, cy + 10, 80, 60);
        g2.fillRect(cx + 110, cy + 10, 80, 60);
        g2.fillRect(cx + 210, cy + 10, 80, 60);

        // Líneas de código en las pantallas
        g2.setColor(Color.GREEN);
        for (int i = 0; i < 4; i++) {
            g2.drawLine(cx + 15, cy + 20 + i * 10, cx + 85, cy + 20 + i * 10);
            g2.drawLine(cx + 115, cy + 20 + i * 10, cx + 185, cy + 20 + i * 10);
            g2.drawLine(cx + 215, cy + 20 + i * 10, cx + 285, cy + 20 + i * 10);
        }

        // Teclado
        g2.setColor(new Color(80, 80, 90));
        g2.fillRoundRect(cx + 50, cy + 80, 200, 30, 10, 10);

        // Silla
        g2.setColor(new Color(60, 60, 70));
        g2.fillRect(cx + 120, cy + 120, 60, 10);
        g2.fillRect(cx + 140, cy + 130, 20, 30);
    }
}