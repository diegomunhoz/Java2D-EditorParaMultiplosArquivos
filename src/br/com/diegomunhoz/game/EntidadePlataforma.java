package br.com.diegomunhoz.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class EntidadePlataforma extends Entidade {

    BufferedImage sprite;

    public EntidadePlataforma(int x, int y, int width, int height) {
        super(x, y);
        pos.setRect(x, y, width, height);
    }

    @Override
    public void init() {
        // O sprite da plataforma consiste em uma imagem com um retângulo cinza.
        sprite = new BufferedImage((int) pos.width, (int) pos.height,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = sprite.getGraphics();
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void update(int currentTick) {
        // Plataformas não possuem lógica: ficam paradas no lugar.
    }

    @Override
    public void render(Graphics2D g) {
        // Desenha o sprite da plataforma.
        g.drawImage(sprite, (int) pos.x, (int) pos.y, null);
    }
}
