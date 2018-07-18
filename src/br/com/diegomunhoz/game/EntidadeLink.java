package br.com.diegomunhoz.game;

import java.awt.Color;
import java.awt.Graphics2D;

public class EntidadeLink extends Entidade {

    String levelFileName;

    public EntidadeLink(int x, int y, int width, int height,
            String fileName) {
        super(x, y);
        pos.setRect(x, y, width, height);
        levelFileName = fileName;
    }

    @Override
    public void init() {
    }

    @Override
    public void update(int currentTick) {
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.white);
        g.draw(pos);
        g.drawString(levelFileName, (int) pos.x + 5, (int) pos.y + 15);
    }
}
