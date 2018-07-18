package br.com.diegomunhoz.game;

import br.com.diegomunhoz.core.DataManager;
import br.com.diegomunhoz.core.Game;
import br.com.diegomunhoz.core.InputManager;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class EditorPlataforma extends Game {

    ArrayList<Entidade> entidades;
    Entidade entidadeSelecionada;
    boolean criandoEntidade;
    boolean gameOver;
    Point rolagem;

    public EditorPlataforma() {
        entidades = new ArrayList<Entidade>();
        criandoEntidade = false;
        gameOver = false;
        rolagem = new Point(0, 0);
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onUnload() {
    }

    @Override
    public void onUpdate(int currentTick) {
        for (Entidade e : entidades) {
            e.update(currentTick);
        }
        if (InputManager.getInstance().isJustPressed(KeyEvent.VK_ESCAPE)) {
            terminate();
        }
        if (InputManager.getInstance().isJustPressed(KeyEvent.VK_DELETE)) {
            if (entidadeSelecionada != null) {
                entidades.remove(entidadeSelecionada);
                entidadeSelecionada = null;
            }
        }
        if (InputManager.getInstance().isJustPressed(KeyEvent.VK_ENTER)) {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                EntidadeLink temp = new EntidadeLink(0, 0, 0, 0, fc.
                        getSelectedFile().getName());
                temp.pos.setRect(entidadeSelecionada.pos);
                entidades.remove(entidadeSelecionada);
                entidadeSelecionada = temp;
                entidadeSelecionada.init();
                entidades.add(entidadeSelecionada);
            }
        }
        if (InputManager.getInstance().isJustPressed(KeyEvent.VK_N)) {
            novo();
        }
        if (InputManager.getInstance().isJustPressed(KeyEvent.VK_S)) {
            salva();
        }
        if (InputManager.getInstance().isJustPressed(KeyEvent.VK_C)) {
            carrega();
        }
        if (InputManager.getInstance().isPressed(KeyEvent.VK_RIGHT)) {
            rolagem.x += 10;
        }
        if (InputManager.getInstance().isPressed(KeyEvent.VK_LEFT)) {
            rolagem.x -= 10;
        }
        if (InputManager.getInstance().isPressed(KeyEvent.VK_DOWN)) {
            rolagem.y += 10;
        }
        if (InputManager.getInstance().isPressed(KeyEvent.VK_UP)) {
            rolagem.y -= 10;
        }

        if (criandoEntidade) {
            if (InputManager.getInstance().isMousePressed(
                    MouseEvent.BUTTON1)) {
                entidadeSelecionada.pos.width = InputManager.getInstance().
                        getMouseX() - entidadeSelecionada.pos.x + rolagem.x;
                entidadeSelecionada.pos.height = InputManager.getInstance().
                        getMouseY() - entidadeSelecionada.pos.y + rolagem.y;
            } else {
                if (entidadeSelecionada.pos.width < 10
                        || entidadeSelecionada.pos.height < 10) {
                    entidades.remove(entidadeSelecionada);
                } else {
                    entidadeSelecionada.init();
                }
                criandoEntidade = false;
            }
        } else if (InputManager.getInstance().isMousePressed(
                MouseEvent.BUTTON1)) {
            Entidade temp = null;
            for (Entidade e : entidades) {
                if (e.pos.contains(InputManager.getInstance().getMouseX()
                        + rolagem.x, InputManager.getInstance().getMouseY()
                        + rolagem.y)) {
                    temp = e;
                }
            }
            if (temp != null) {
                entidadeSelecionada = temp;
            } else {
                entidadeSelecionada = new EntidadePlataforma(InputManager.
                        getInstance().getMouseX() + rolagem.x, InputManager.
                                getInstance().getMouseY() + rolagem.y, 10, 10);
                entidadeSelecionada.init();
                entidades.add(entidadeSelecionada);
                criandoEntidade = true;
            }
        }
    }

    @Override
    public void onRender(Graphics2D g) {
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, 800, 600);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(-rolagem.x, -rolagem.y);
        for (Entidade e : entidades) {
            e.render(g2);
        }
        g2.setColor(Color.white);
        if (entidadeSelecionada != null) {
            g2.draw(entidadeSelecionada.pos);
        }

        g.setColor(Color.white);
        g.drawString("Rolagem: (" + rolagem.x + "," + rolagem.y + ")", 200,
                20);
        if (criandoEntidade) {
            g.drawString("CRIANDO PLATAFORMA", 350, 20);
        }
        g.drawString("[N] Novo    [S] Salvar    [C] Carregar    [ESC] Sair",
                520, 20);
    }

    public void novo() {
        entidadeSelecionada = null;
        entidades.clear();
        rolagem = new Point(0, 0);
    }

    public void salva() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            salvaArquivo(fc.getSelectedFile().toURI());
        }
    }

    public void carrega() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            carregaArquivo(fc.getSelectedFile().toURI());
        }
    }

    public void salvaArquivo(URI fileURI) {
        try {
            Point2D.Double leftMost = new Point2D.Double(0, 0);
            for (int i = 0; i < entidades.size(); i++) {
                Entidade e = entidades.get(i);
                if (leftMost.x > e.pos.x) {
                    leftMost.x = e.pos.x;
                }
                if (leftMost.y > e.pos.y) {
                    leftMost.y = e.pos.y;
                }
            }
            DataManager dm = new DataManager(fileURI);
            dm.write("plataformas", entidades.size());
            for (int i = 0; i < entidades.size(); i++) {
                Entidade e = entidades.get(i);
                dm.write("plataforma." + i + ".x", (int) e.pos.x
                        - (int) leftMost.x);
                dm.write("plataforma." + i + ".y", (int) e.pos.y
                        - (int) leftMost.y);
                dm.write("plataforma." + i + ".width", (int) e.pos.width);
                dm.write("plataforma." + i + ".height", (int) e.pos.height);
                if (e instanceof EntidadeLink) {
                    dm.write("plataforma." + i + ".levelFileName",
                            ((EntidadeLink) e).levelFileName);
                }
            }
            dm.save();
        } catch (IOException ex) {
            Logger.getLogger(EditorPlataforma.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }

    public void carregaArquivo(URI fileURI) {
        try {
            DataManager dm = new DataManager(fileURI);
            novo();
            int qtd = 0;
            qtd = dm.read("plataformas", qtd);
            for (int i = 0; i < qtd; i++) {
                String fn = null;
                fn = dm.read("plataforma." + i + ".levelFileName", fn);
                Entidade e = null;
                if (fn == null) {
                    e = new EntidadePlataforma(0, 0, 0, 0);
                } else {
                    e = new EntidadeLink(0, 0, 0, 0, fn);
                }
                e.pos.x = dm.read("plataforma." + i + ".x", (int) e.pos.x);
                e.pos.y = dm.read("plataforma." + i + ".y", (int) e.pos.y);
                e.pos.width = dm.read("plataforma." + i + ".width",
                        (int) e.pos.width);
                e.pos.height = dm.read("plataforma." + i + ".height",
                        (int) e.pos.height);
                e.init();
                entidades.add(e);
            }
        } catch (Exception ex) {
            // Se não conseguir ler (der erro), nada faz.
        }
    }
}
