package joot.m2.client.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * 场景基类
 * 
 * @author linxing
 *
 */
abstract class BaseScene implements Screen {
	// 舞台
	protected Stage stage;
	
	public BaseScene(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void show() {
		if (stage != null)
			Gdx.input.setInputProcessor(stage);
	}
	
	protected abstract boolean initializeComponents();

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (!initializeComponents()) return;
		if (stage != null) {
			stage.act(delta);
			stage.draw();
		}
	}

	@Override
	public void resize(int width, int height) {
		if (stage != null)
			stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() {
		stage.clear();
	}

	@Override
	public void dispose() { }

}
