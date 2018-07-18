package br.com.diegomunhoz.game;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class Entidade {

    // Constantes para identificar o significado de cada item do array de colisores.
    static final int COLLIDING_ABOVE = 0; // acima
    static final int COLLIDING_RIGHT = 1; // direita
    static final int COLLIDING_BELOW = 2; // abaixo
    static final int COLLIDING_LEFT = 3; // esquerda
    Rectangle2D.Double pos;
    int energia;
    Entidade[] collidingEntities;

    public Entidade(int x, int y) {
        pos = new Rectangle2D.Double(x, y, 1, 1);
        this.energia = 1;
        collidingEntities = new Entidade[4];
    }

    public abstract void init();

    public abstract void update(int currentTick);

    public abstract void render(Graphics2D g);
}
