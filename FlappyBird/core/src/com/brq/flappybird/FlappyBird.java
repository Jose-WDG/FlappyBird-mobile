package com.brq.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    private SpriteBatch batch;

    private Texture[] passaro;
    private Texture fundo;
    private Texture canoTopo;
    private Texture canoBaixo;
    private Texture gameOver;
    private float variacao = 0;
    private Random numeroRandom;
    private BitmapFont fonte;
    private BitmapFont menssagem;
    private int pontuacao = 0;
    private Circle passaroCirculo;
    private Rectangle retangoloCanoTopo;
    private Rectangle retanguloCanoBaixo;
    //private ShapeRenderer shape;

    //Atributos de configurações

    private int tamanhoTelaHorizontal;
    private int tamanhoTelaVertical;
    private int velocidadeDeQueda = 0;
    private int posicaoInicialVertical = 0;
    private int posicaoMovimentoCanoHorizontal;
    private int estadoDoJogo = 0;//IF O JOGO NÃO INICIA, IF 1 INICIA,if 2 GAME OVER

    private float espacoEmtreCanos;
    private float deltaTime;
    private float alturaEntreCanosRandom;
    private Boolean marcouPonto = false;


    @Override
    public void create() {
        batch = new SpriteBatch();
        numeroRandom = new Random();
        passaroCirculo = new Circle();
        //retangoloCanoTopo = new Rectangle();
        //retanguloCanoBaixo = new Rectangle();
        //shape = new ShapeRenderer();
        fonte = new BitmapFont();
        menssagem = new BitmapFont();
        menssagem.setColor(Color.WHITE);
        menssagem.getData().setScale(3);
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(6);
        passaro = new Texture[3];
        passaro[0] = new Texture("passaro1.png");
        passaro[1] = new Texture("passaro2.png");
        passaro[2] = new Texture("passaro3.png");
        fundo = new Texture("fundo.png");
        canoBaixo = new Texture("cano_baixo.png");
        canoTopo = new Texture("cano_topo.png");
        gameOver = new Texture("game_over.png");
        tamanhoTelaHorizontal = Gdx.graphics.getBackBufferWidth();
        tamanhoTelaVertical = Gdx.graphics.getBackBufferHeight();
        posicaoInicialVertical = tamanhoTelaVertical / 2;
        posicaoMovimentoCanoHorizontal = tamanhoTelaHorizontal + canoTopo.getWidth();
        espacoEmtreCanos = posicaoInicialVertical;

    }

    @Override
    public void render() {
        deltaTime = Gdx.graphics.getDeltaTime();
        variacao += deltaTime * 7;
        if (variacao > 2) {
            variacao = 0;
        }
        if (estadoDoJogo == 0) {
            if (Gdx.input.justTouched()) {
                estadoDoJogo = 1;
            }
        } else {
            velocidadeDeQueda++;
            posicaoInicialVertical -= velocidadeDeQueda;
            if (posicaoInicialVertical < 0) {
                posicaoInicialVertical = 0;
            }

            if (estadoDoJogo == 1) {
                posicaoMovimentoCanoHorizontal -= deltaTime * 700;
                if (Gdx.input.justTouched()) {
                    velocidadeDeQueda = -20;
                }

                if (posicaoMovimentoCanoHorizontal < -150) {
                    posicaoMovimentoCanoHorizontal = tamanhoTelaHorizontal;
                    alturaEntreCanosRandom = numeroRandom.nextInt(400) - 200;
                    marcouPonto = false;
                }
                //verifica pontuação
                if (posicaoMovimentoCanoHorizontal < 120) {
                    if (!marcouPonto) {
                        marcouPonto = true;
                        pontuacao++;
                    }

                }
            } else {//tela de game over
                if (Gdx.input.justTouched()) {
                    estadoDoJogo = 0;
                    pontuacao = 0;
                    velocidadeDeQueda = 0;
                    posicaoInicialVertical = tamanhoTelaVertical/2;
                    posicaoMovimentoCanoHorizontal = tamanhoTelaHorizontal+150;
                }
            }
        }
        batch.begin();//INICIO

        batch.draw(fundo, 0, 0, tamanhoTelaHorizontal, tamanhoTelaVertical);
        batch.draw(canoTopo, posicaoMovimentoCanoHorizontal,
                tamanhoTelaVertical / 2 + espacoEmtreCanos / 2 + alturaEntreCanosRandom,
                150, canoTopo.getHeight());

        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal,
                tamanhoTelaVertical / 2 - canoBaixo.getHeight() - espacoEmtreCanos / 2 - alturaEntreCanosRandom, 150, canoBaixo.getHeight());

        batch.draw(passaro[(int) variacao], 120, posicaoInicialVertical);
        fonte.draw(batch, String.valueOf(pontuacao), tamanhoTelaHorizontal / 2, tamanhoTelaVertical - 52);
        if (estadoDoJogo == 2) {
            batch.draw(gameOver, tamanhoTelaHorizontal / 2 - gameOver.getWidth() / 2, tamanhoTelaVertical / 2);
            menssagem.draw(batch, "Toque para Reiniciar", tamanhoTelaHorizontal / 2 - gameOver.getWidth() / 2, tamanhoTelaVertical / 2 - 30);

        }
        batch.end();//FIM

        passaroCirculo.set(120 + passaro[0].getWidth() / 2, posicaoInicialVertical + passaro[0].getHeight() / 2, passaro[0].getWidth() / 2);
        retanguloCanoBaixo = new Rectangle(
                posicaoMovimentoCanoHorizontal,
                tamanhoTelaVertical / 2 - canoBaixo.getHeight() - espacoEmtreCanos / 2 - alturaEntreCanosRandom,
                150,
                canoBaixo.getHeight());
        retangoloCanoTopo = new Rectangle(
                posicaoMovimentoCanoHorizontal,
                tamanhoTelaVertical / 2 + espacoEmtreCanos / 2 + alturaEntreCanosRandom,
                150,
                canoTopo.getHeight());
        //shape.begin(ShapeRenderer.ShapeType.Filled);
        //shape.circle(passaroCirculo.x,passaroCirculo.y,passaroCirculo.radius);
        //shape.rect(retanguloCanoBaixo.x,retanguloCanoBaixo.y,retanguloCanoBaixo.width,retanguloCanoBaixo.height);
        //shape.rect(retangoloCanoTopo.x,retangoloCanoTopo.y,retangoloCanoTopo.width,retangoloCanoTopo.height);
        //shape.setColor(Color.RED);
        //shape.end();

        if (Intersector.overlaps(passaroCirculo, retanguloCanoBaixo) ||
                Intersector.overlaps(passaroCirculo, retangoloCanoTopo)||
                posicaoInicialVertical<=0 || posicaoInicialVertical >= tamanhoTelaVertical ) {

            estadoDoJogo = 2;
        }
    }

}
