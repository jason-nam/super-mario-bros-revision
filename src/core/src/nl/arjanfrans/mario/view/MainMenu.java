package nl.arjanfrans.mario.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;

public class MainMenu implements Screen {

    private SpriteBatch batch;
    private Viewport viewport;
    private OrthographicCamera camera;
//    private TextureAtlas atlas;
    private Game mainGame;
    protected Stage stage;
//    protected Skin skin;

    public MainMenu(Game game) {
        mainGame = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(500,500, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();
//        Table topTable = new Table();

        //Set table to fill stage
        mainTable.setFillParent(true);
//        mainTable.setBackground()
        //Set alignment of contents in the table.
//        mainTable.top();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.fontColor = new Color(Color.WHITE);
        style.font = new BitmapFont();
        style.font.scale((float) 0.5);

        // Create buttons
//        TextButton playButton = new TextButton("Play", style);
        TextButton exit = new TextButton("Exit", style);
        TextButton one_player_game = new TextButton("1 PLAYER GAME", style);
        TextButton two_player_game = new TextButton("2 PLAYER GAME", style);
//        TextButton character = new TextButton("MARIO", style);
//        TextButton world = new TextButton("WORLD\n1-1", style);
//        TextButton time = new TextButton("TIME", style);

        //Add listeners to buttons
        one_player_game.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainGame.setScreen(new Screen() {
                    @Override
                    public void show() {}
                    @Override
                    public void render(float delta) {}
                    @Override
                    public void resize(int width, int height) {}
                    @Override
                    public void pause() {}
                    @Override
                    public void resume() {}
                    @Override
                    public void hide() {}
                    @Override
                    public void dispose() {}
                });
            }
        });
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Add buttons to table
        mainTable.add(one_player_game);
        mainTable.row();
        mainTable.add(two_player_game);
        mainTable.row();
        mainTable.add(exit);
//        mainTable.row();

        mainTable.align(Align.center);

//        topTable.add(character);
//        topTable.add(world);
//        topTable.add(time);

//        topTable.align(Align.top);

        //Add table to stage
        stage.addActor(mainTable);
//        stage.addActor(topTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Texture bg = new Texture("data/backgrounds/MAIN_MENU_BACKGROUND.png");
        Texture logo = new Texture("data/images/LOGO.png");

        batch.begin();
        batch.draw(bg, 0,0, 500, 500);
        batch.draw(logo, 125,325, 250, 150);
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
//        skin.dispose();
//        atlas.dispose();
    }
}
