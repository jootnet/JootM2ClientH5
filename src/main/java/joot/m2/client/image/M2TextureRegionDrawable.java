package joot.m2.client.image;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class M2TextureRegionDrawable extends TextureRegionDrawable {

	public M2TextureRegionDrawable(TextureRegion texture, boolean blend) {
		super(texture);
		this.blend = blend;
	}
	
	/** 是否启用混合 */
	private final boolean blend;
	
	@Override
	public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height,
			float scaleX, float scaleY, float rotation) {
		if (blend) {
			batch.setBlendFunction(GL20.GL_SRC_COLOR, GL20.GL_ONE);
		}
		super.draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
		if (blend) {
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}
	}
	
	@Override
	public void draw(Batch batch, float x, float y, float width, float height) {
		if (blend) {
			batch.setBlendFunction(GL20.GL_SRC_COLOR, GL20.GL_ONE);
		}
		super.draw(batch, x, y, width, height);
		if (blend) {
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}
	}
}
