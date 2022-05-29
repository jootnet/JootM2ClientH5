package joot.m2.client.ui;

import static joot.m2.client.util.DrawingUtil.transDraw;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.github.jootnet.m2.core.actor.ChrBasicInfo;
import com.github.jootnet.m2.core.actor.Direction;

import joot.m2.client.image.Images;
import joot.m2.client.image.M2Texture;
import joot.m2.client.map.Map;
import joot.m2.client.map.Maps;
import joot.m2.client.util.DrawingUtil.RectI;

/**
 * 地图绘制对象
 */
public final class MapActor extends WidgetGroup implements PropertyChangeListener {
	private String mapNo;
	
	/** 当前显示地图对象 */
	private Map map;
	/** 当前角色坐标（地图中心） */
	private int roleX;
	private int roleY;
	/** 当前角色绘制偏移量 */
	private int shiftX;
	private int shiftY;
	
	/** 当前地图的所有人(按地图行存储，达到存取时间复杂度最低，绘制最优) */
	private List<ChrBasicInfo>[] hums;
	private List<ChrBasicInfo> tempHums = new ArrayList<>();
	
	/**
	 * 进入地图
	 * 
	 * @param mapNo 地图编号
	 * @return 当前对象
	 */
	public MapActor enter(String mapNo) {
		if (mapNo != null && mapNo.equals(this.mapNo)) return this;
		this.mapNo = mapNo;
		map = null;
		hums = null;
		tempHums.clear();
		return this;
	}
	
	/**
	 * 玩家进入地图
	 * 
	 * @param hum 玩家
	 * @return 当前对象
	 */
	public MapActor add(ChrBasicInfo hum) {
		if (hums != null) {
			hums[hum.y].add(hum);
		} else {
			tempHums.add(hum);
		}
		hum.addPropertyChangeListener(this);
		return this;
	}
	
	/**
	 * 玩家离开地图
	 * 
	 * @param hum 玩家
	 * @return 当前对象
	 */
	public MapActor remove(ChrBasicInfo hum) {
		if (hums != null) {
			hums[hum.y].remove(hum);
		} else {
			tempHums.remove(hum);
		}
		hum.removePropertyChangeListener(this);
		return this;
	}
	
	/**
	 * 设置地图展示中心坐标
	 * <br>
	 * 即是当前玩家游戏坐标
	 * 
	 * @param roleX 游戏坐标x
	 * @param roleY 游戏坐标y
	 * @return 当前对象
	 */
	public MapActor setCenter(int roleX, int roleY) {
		this.roleX = roleX;
		this.roleY = roleY;
		return this;
	}

	/**
	 * 设置(主)玩家角色当前绘制偏移量
	 * 
	 * @param shiftX 角色绘制横向偏移量
	 * @return 当前对象
	 */
	public MapActor setShiftX(int shiftX) {
		this.shiftX = -shiftX;
		return this;
	}

	/**
	 * 设置(主)玩家角色当前绘制偏移量
	 *
	 * @param shiftY 角色绘制纵向偏移量
	 * @return 当前对象
	 */
	public MapActor setShiftY(int shiftY) {
		this.shiftY = -shiftY;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void act(float delta) {
		if (map == null) {
			if (mapNo != null) {
				map = Maps.get(mapNo);
				if (map != null) {
					hums = new List[map.height + 1];
					for (int h = 0; h < hums.length; ++h) {
						hums[h] = new ArrayList<>();
					}
					for (ChrBasicInfo hum : tempHums) {
						hums[hum.y].add(hum);
					}
					tempHums.clear();
				}
			}
		} else {
			RectI pixel = new RectI();
			RectI game = new RectI();
			calcCache(pixel, game);
			for (int w = 0; w < game.width; ++w) {
				for (int h = 0; h < game.height; ++h) {
					String tileFileName = map.tilesFileName[game.x + w][game.y + h];
					String smTileFileName = map.smTilesFileName[game.x + w][game.y + h];
					String objFileName = map.objsFileName[game.x + w][game.y + h];
					if (tileFileName != null) {
						M2Texture[] tileTex = Images.get(tileFileName);
						if (tileTex != null) {
							map.tilesFileName[game.x + w][game.y + h] = null;
							map.tilesTexture[game.x + w][game.y + h] = tileTex[0];
						}
					}
					if (smTileFileName != null) {
						M2Texture[] smTileTex = Images.get(smTileFileName);
						if (smTileTex != null) {
							map.smTilesFileName[game.x + w][game.y + h] = null;
							map.smTilesTexture[game.x + w][game.y + h] = smTileTex[0];
						}
					}
					if (objFileName != null) {
						M2Texture[] objTex = Images.get(objFileName);
						if (objTex != null) {
							map.objsFileName[game.x + w][game.y + h] = null;
							// 这里是我的一个灵感，我将原来的m2中纵向横跨多个地图块的obj图像以地图块高度拆分到多个
							// 即向上分，比如我们在（1，10）处有一个48*160的obj图像，则把这幅图分到（1，10）=>（1，6）
							// 这样有好处，绘制的时候不需要多画（原来必须向下多画一些才能保证视区元素完整），更好做半透明（当人物在树下房檐下时更好针对某个地图块做半透明）
							//	唯一可能出现的麻烦是多层问题。比如原来（1，9）处也有一个超出32高度的obj
							int upCellCount = (int) Math.ceil(objTex[0].getHeight() / 32.f);
							for (int upIdx = 0; upIdx < upCellCount; ++upIdx) {
								TextureRegion region = new TextureRegion(objTex[0], 0, 
										Math.max(0, objTex[0].getHeight() - (upIdx + 1) * 32),
										objTex[0].getWidth(),
										Math.min(32, objTex[0].getHeight() - upIdx * 32));
								map.addObjTextureRegion(game.x + w, game.y - upIdx + h, region, game.y + h);
							}
						}
					}
				}
			}
		}
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (map != null) {
			RectI pixel = new RectI();
			RectI game = new RectI();
			calcDisplay(pixel, game);
			
			int drawingX = pixel.x;
			int drawingY = pixel.y;
			for (int w = 0; w < game.width; ++w) {
				drawingY = pixel.y;
				for (int h = 0; h < game.height; ++h) {
					Texture tile = map.tilesTexture[game.x + w][game.y + h];
					// 绘制大地砖
					if (tile != null) {
						transDraw(batch, (int) getWidth(), (int) getHeight(), tile, drawingX, drawingY);
					}
					drawingY += 32;
				}
				drawingX += 48;
			}
			
			drawingX = pixel.x;
			for (int w = 0; w < game.width; ++w) {
				drawingY = pixel.y;
				for (int h = 0; h < game.height; ++h) {
					Texture smTile = map.smTilesTexture[game.x + w][game.y + h];
					// 绘制小地砖
					if (smTile != null) {
						transDraw(batch, (int) getWidth(), (int) getHeight(), smTile, drawingX, drawingY);
					}
					drawingY += 32;
				}
				drawingX += 48;
			}
			
			// TODO 半透明，opengl这里我真不知道怎么做半透明了。如果我猜得没错的话应该是先用某种混合把obj图和人物图先合成，再叠到画布上。但我不会……
			for (int gamey = game.y; gamey <= map.height; ++gamey) {
				// 在同一行，人物应该把对象层踩在脚下或挡在身后
				// 同时人物会被下一行的建筑物挡住
				
				// 绘制树木，建筑物等
				List<TextureRegion> rgs = map.getObjsTextureRegion(gamey);
				for (TextureRegion objRegion : rgs) {
					int[] xy = map.getObjTextureRegion(objRegion);
					if (xy[0] < game.x || xy[0] > game.x + game.width - 1) continue;
					if (xy[1] < game.y || xy[1] > game.y + game.height - 1) continue;
					drawingX = pixel.x + (xy[0] - game.x) * 48;
					drawingY = pixel.y + (xy[1] - game.y) * 32;
					transDraw(batch, (int) getWidth(), (int) getHeight(), objRegion, drawingX, drawingY
							+ 32 - objRegion.getRegionHeight()); // 32 - height是最上面的那个拆分块的空白高度
				}
				
				// 绘制人物
				List<ChrBasicInfo> yHums = hums[gamey];
				for (ChrBasicInfo hum : yHums) {
					int x = hum.x;
					int y = hum.y;
					if (x < game.x || x > game.x + game.width - 1) continue;
					if (y < game.y || y > game.y + game.height - 1) continue; // FIXME 这里需要修改一下，不然的话屏幕外的“半个人”不会显示
					drawingX = pixel.x + (x - game.x) * 48;
					drawingY = pixel.y + (y - game.y) * 32;
					List<java.util.Map.Entry<M2Texture, Boolean>> humTexs = new LinkedList<>(); // 纹理以及是否需要混合。翅膀和光剑需要混合
					M2Texture texDress = Images.getDress(hum);
					M2Texture texEquip = Images.getWeapon(hum);
					M2Texture texWing = Images.getHumEffect(hum);
					if (hum.action.dir == Direction.West || hum.action.dir == Direction.NorthWest) {
						// 人物朝向左方或左上方时先绘制武器，不然会穿模
						if (texEquip != null)
							humTexs.add(new SimpleEntry<M2Texture, Boolean>(texEquip, false));
						if (texDress != null)
							humTexs.add(new SimpleEntry<M2Texture, Boolean>(texDress, false));
					} else {
						if (texDress != null)
							humTexs.add(new SimpleEntry<M2Texture, Boolean>(texDress, false));
						if (texEquip != null)
							humTexs.add(new SimpleEntry<M2Texture, Boolean>(texEquip, false));
					}
					if (texWing != null)
						humTexs.add(new SimpleEntry<M2Texture, Boolean>(texWing, true));
					for (java.util.Map.Entry<M2Texture, Boolean> tex_blend : humTexs) {
						if (tex_blend.getValue()) {
							batch.setBlendFunction(GL20.GL_SRC_COLOR, GL20.GL_ONE);
						}
						transDraw(batch, (int) getWidth(), (int) getHeight(), tex_blend.getKey(),
								drawingX + tex_blend.getKey().getOffsetX() + hum.shiftX,
								drawingY + tex_blend.getKey().getOffsetY() + hum.shiftY);
						if (tex_blend.getValue()) {
							batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
						}
					}
				}
			}
		}
		super.draw(batch, parentAlpha);
	}
	
	/**
	 * 通过地图中心位置和画布大小计算绘制区域和地图展示区域
	 * <br>
	 * 会向外延申一些，用作缓存
	 * 
	 * @param pixel 画布需要绘制内容的区域
	 * @param game 地图需要绘制到画布的区域
	 */
	private void calcCache(RectI pixel, RectI game) {
		// 画布横向可容纳的一半地图块数量（为了视觉效果更好，先减去一个用于绝对居中的块）
		int maxHalfCellH = (int) Math.ceil(((int) getWidth() - 48) / 2 / 48);
		// 画布纵向可容纳的一半地图块数量（为了视觉效果更好，先减去一个用于绝对居中的块）
		int maxHalfCellV = (int) Math.ceil(((int) getHeight() - 32) / 2 / 32);
		
		// 从画布中间往左最多能延申的块数量（-10是冗余缓存量）
		int leftMinCell = roleX - maxHalfCellH - 10;
		for(;leftMinCell < 1;++leftMinCell); // 避免溢出，最小为1
		// 从画布中间往上最多能延申的块数量（-10是冗余缓存量）
		int topMinCell = roleY - maxHalfCellV - 10;
		for(;topMinCell < 1;++topMinCell);
		// 从画布中间往右最多能延申的块数量（-10是冗余缓存量）
		int rightMaxCell = roleX + maxHalfCellH + 10;
		for(;rightMaxCell > map.width;--rightMaxCell);
		// 从画布中间往下最多能延申的块数量（-50是冗余缓存量）
		int bottomMaxCell = roleY + maxHalfCellV + 50;
		for(;bottomMaxCell > map.height;--bottomMaxCell);
		
		game.x = leftMinCell;
		game.y = topMinCell;
		game.width = rightMaxCell - leftMinCell + 1;
		game.height = bottomMaxCell - topMinCell + 1;
		
		pixel.x = ((int) getWidth() - 48) / 2 - (roleX - leftMinCell) * 48; 
		pixel.y = ((int) getHeight() - 32) / 2 - (roleY - topMinCell) * 32;
		pixel.width = (rightMaxCell - leftMinCell + 1) * 48;
		pixel.height = (bottomMaxCell - topMinCell + 1) * 32;
	}
	
	/**
	 * 通过地图中心位置和画布大小计算绘制区域和地图展示区域
	 * <br>
	 * 将不会向外延申一些
	 * 
	 * @param pixel 画布需要绘制内容的区域
	 * @param game 地图需要绘制到画布的区域
	 */
	private void calcDisplay(RectI pixel, RectI game) {
		// 为了绝对居中，都是先减去一个绝对居中的块
		int gameLeft = roleX - 1; // 往左移动一格开始计算起始区域
		int pixelLeft = (int) getWidth() / 2 - 24 - 48; // 可视区域左侧还剩的像素（往左移动一格后）
		for (;gameLeft > 1 && pixelLeft >= 0; --gameLeft, pixelLeft -= 48);
		game.x = gameLeft;
		pixel.x = pixelLeft + shiftX; // 加上地图卷动偏移量
		
		int gameRight = roleX + 1;
		int pixelRight = (int) getWidth() / 2 + 24 + 48;
		for (;gameRight <= map.width && pixelRight <= (int) getWidth(); ++gameRight, pixelRight += 48);
		game.width = gameRight - gameLeft + 1; // 加上中间的块
		pixel.width = pixelRight - pixelLeft; // 由于算的是边界，不需要加中间块
		
		int gameTop = roleY - 1;
		int pixelTop = (int) getHeight() / 2 - 16 - 32;
		for (;gameTop > 1 && pixelTop >= 0; --gameTop, pixelTop -= 32);
		game.y = gameTop;
		pixel.y = pixelTop + shiftY;
		
		int gameBottom = roleY + 1;
		int pixelBottom = (int) getHeight() / 2 + 16 + 32;
		for (;gameBottom <= map.height && pixelBottom <= (int) getHeight(); ++gameBottom, pixelBottom += 32);
		game.height = gameBottom - gameTop + 1;
		pixel.height = pixelBottom - pixelTop;
		
		// 地图卷动时需要多绘制一些，避免黑边
		if (shiftX < 0) {
			// 地图向左偏移时，需要多绘制右边，不然有黑边
			if (game.x + game.width <= map.width) {
				game.width++;
				pixel.width += 48;
			}
			if (shiftX < -48) {
				if (game.x + game.width <= map.width) {
					game.width++;
					pixel.width += 48;
				}
			}
		} else if (shiftX > 0) {
			// 地图向右偏移时，需要多绘制左边，不然有黑边
			if (game.x > 1) {
				game.x--;
				pixel.x -= 48;
				game.width++;
				pixel.width += 48;
			}
			if (shiftX > 48) {
				if (game.x > 1) {
					game.x--;
					pixel.x -= 48;
					game.width++;
					pixel.width += 48;
				}
			}
		}
		if (shiftY < 0) {
			if (game.y + game.height <= map.height) {
				game.height++;
				pixel.height += 32;
			}
			if (shiftY < -32) {
				if (game.y + game.height <= map.height) {
					game.height++;
					pixel.height += 32;
				}
			}
		} else if (shiftY > 0) {
			if (game.y > 1) {
				game.y--;
				pixel.y -= 32;
				game.height++;
				pixel.height += 32;
			}
			if (shiftY > 32) {
				if (game.y > 1) {
					game.y--;
					pixel.y -= 32;
					game.height++;
					pixel.height += 32;
				}
			}
		}


		if (game.x % 2 == 0) {
			// 保证第一列不是双数，不然黑边
			if (game.x > 1) {
				game.x -= 1;
				game.width += 1;
				pixel.x -= 48;
				pixel.width += 48;
			}
		}

		if (game.y % 2 == 0) {
			// 保证第一行不是双数，不然黑边
			if (game.y > 1) {
				game.y -= 1;
				game.height += 1;
				pixel.y -= 32;
				pixel.height += 32;
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() instanceof ChrBasicInfo && evt.getPropertyName() == "y") {
			int sy = ((Integer)evt.getOldValue()).intValue();
			int ny = ((Integer)evt.getNewValue()).intValue();
			if (hums != null) {
				hums[sy].remove(evt.getSource());
				hums[ny].add((ChrBasicInfo) evt.getSource());
			}
		}
	}

	/**
	 * 获取地图块可移动标记
	 * 
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @return 是否可移动
	 */
	public boolean isCanWalk(int x, int y) {
		if (map == null) return false;
		return map.canWalk[x][y];
	}
	
	/**
	 * 角色坐标转地图坐标
	 * <br>
	 * 比如盟重，角色坐标是从(1,1)到(1000,1000)
	 * <br>
	 * 地图坐标是指左上角坐标系下的画布像素坐标（地图块的起始坐标）
	 * 
	 * @param x 角色坐标
	 * @param y 角色坐标
	 * @return 对应地图上的画布像素坐标
	 */
	public int[] humXY2MapXY(int x, int y) {
		int[] ret = new int[2];
		if (map == null) {
			ret[0] = 48;
			ret[1] = 32;
			return ret;
		}
		RectI pixel = new RectI();
		RectI game = new RectI();
		calcDisplay(pixel, game);
		ret[0] = pixel.x + (x - game.x) * 48;
		ret[1] = pixel.y + (y - game.y) * 32;
		return ret;
	}
}
