package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.github.jootnet.m2.core.actor.AttackMode;
import com.github.jootnet.m2.core.actor.ChrBasicInfo;
import com.github.jootnet.m2.core.actor.ChrPrivateInfo;
import com.github.jootnet.m2.core.actor.ChrPublicInfo;
import com.github.jootnet.m2.core.actor.Occupation;
import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 进入游戏响应
 * <br>
 * 这个数据包会发送给当前客户端以及地图内其他玩家
 * <br>
 * 当前客户端如果收到forbidTip，则证明角色不可用，可能被封禁
 * <br>
 * 	其他玩家收到的forbidTip永远为null
 */
public class EnterResp extends Message {
	
	static {
		deSerializers.put(MessageType.ENTER_RESP, buffer -> {
			if (!buffer.hasRemaining())
				return null;
			String forbidTip = unpackString(buffer);
			if (forbidTip != null) {
				return new EnterResp(forbidTip, null, null, null);
			}
			return new EnterResp(null, unpackChrBasicInfo(buffer), unpackChrPublicInfo(buffer), unpackChrPrivateInfo(buffer));
		});
	}

	@Override
	public MessageType type() {
		return MessageType.ENTER_RESP;
	}

	/** 角色禁用原因 */
	public String forbidTip;
	/** 角色基础信息 */
	public ChrBasicInfo cBasic;
	/** 角色可探查信息 */
	public ChrPublicInfo cPublic;
	/** 角色私有信息 */
	public ChrPrivateInfo cPrivate;
	
	public EnterResp(String forbidTip, ChrBasicInfo cBasic, ChrPublicInfo cPublic, ChrPrivateInfo cPrivate) {
		this.forbidTip = forbidTip;
		this.cBasic = cBasic;
		this.cPublic = cPublic;
		this.cPrivate = cPrivate;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		packString(forbidTip, buffer);
		if (cBasic != null)
			pack(cBasic, buffer);
		if (cPublic != null)
			pack(cPublic, buffer);
		if (cPrivate != null)
			pack(cPrivate, buffer);
	}
	

	private static void pack(ChrBasicInfo info, DataOutput buffer) throws IOException {
    	if (info == null) return;
    	packString(info.name, buffer);
    	buffer.writeByte(info.gender);
    	buffer.writeInt(info.occupation.ordinal());
    	buffer.writeInt(info.level);
    	buffer.writeInt(info.hp);
    	buffer.writeInt(info.maxHp);
    	buffer.writeInt(info.mp);
    	buffer.writeInt(info.maxMp);
    	buffer.writeInt(info.humFileIdx);
    	buffer.writeInt(info.humIdx);
    	buffer.writeInt(info.humEffectFileIdx);
    	buffer.writeInt(info.humEffectIdx);
    	buffer.writeInt(info.weaponFileIdx);
    	buffer.writeInt(info.weaponIdx);
    	buffer.writeInt(info.weaponEffectFileIdx);
    	buffer.writeInt(info.weaponEffectIdx);
    	buffer.writeInt(info.x);
    	buffer.writeInt(info.y);
    	packString(info.guildName, buffer);
    }
    private static ChrBasicInfo unpackChrBasicInfo(ByteBuffer buffer) {
    	if (!buffer.hasRemaining()) return null;
    	String name = unpackString(buffer);
    	int oi = buffer.getInt();
    	Occupation occ = null;
    	for (Occupation item : Occupation.values()) {
    		if (item.ordinal() == oi) {
    			occ = item;
    			break;
    		}
    	}
    	byte gender = buffer.get();
    	int level = buffer.getInt();
    	int hp = buffer.getInt();
    	int maxHp = buffer.getInt();
    	int mp = buffer.getInt();
    	int maxMp = buffer.getInt();
    	int humFileIdx = buffer.getInt();
    	int humIdx = buffer.getInt();
    	int humEffectFileIdx = buffer.getInt();
    	int humEffectIdx = buffer.getInt();
    	int weaponFileIdx = buffer.getInt();
    	int weaponIdx = buffer.getInt();
    	int weaponEffectFileIdx = buffer.getInt();
    	int weaponEffectIdx = buffer.getInt();
    	int x = buffer.getInt();
    	int y = buffer.getInt();
    	String guildName = unpackString(buffer);
    	return new ChrBasicInfo(name, gender, occ, level, hp, maxHp, mp, maxMp, humFileIdx, humIdx, humEffectFileIdx, humEffectIdx, weaponFileIdx, weaponIdx, weaponEffectFileIdx, weaponEffectIdx, x, y, guildName);
    }
    private static void pack(ChrPublicInfo info, DataOutput buffer) throws IOException {
    	if (info == null) return;
    	buffer.writeInt(info.attackPoint);
    	buffer.writeInt(info.maxAttackPoint);
    	buffer.writeInt(info.magicAttackPoint);
    	buffer.writeInt(info.maxMagicAttackPoint);
    	buffer.writeInt(info.taositAttackPoint);
    	buffer.writeInt(info.maxTaositAttackPoint);
    	buffer.writeInt(info.defensePoint);
    	buffer.writeInt(info.maxDefensePoint);
    	buffer.writeInt(info.magicDefensePoint);
    	buffer.writeInt(info.maxMagicDefensePoint);
    }
    private static ChrPublicInfo unpackChrPublicInfo(ByteBuffer buffer) {
    	if (!buffer.hasRemaining()) return null;
    	int attackPoint = buffer.getInt();
    	int maxAttackPoint = buffer.getInt();
    	int magicAttackPoint = buffer.getInt();
    	int maxMagicAttackPoint = buffer.getInt();
    	int taositAttackPoint = buffer.getInt();
    	int maxTaositAttackPoint = buffer.getInt();
    	int defensePoint = buffer.getInt();
    	int maxDefensePoint = buffer.getInt();
    	int magicDefensePoint = buffer.getInt();
    	int maxMagicDefensePoint = buffer.getInt();
    	return new ChrPublicInfo(attackPoint, maxAttackPoint, magicAttackPoint, maxMagicAttackPoint, taositAttackPoint, maxTaositAttackPoint, defensePoint, maxDefensePoint, magicDefensePoint, maxMagicDefensePoint);
    }
    private static void pack(ChrPrivateInfo info, DataOutput buffer) throws IOException {
    	if (info == null) return;
    	buffer.writeInt(info.exp);
    	buffer.writeInt(info.levelUpExp);
    	buffer.writeInt(info.bagWeight);
    	buffer.writeInt(info.maxBagWeight);
    	buffer.writeInt(info.wearWeight);
    	buffer.writeInt(info.maxWearWeight);
    	buffer.writeInt(info.handWeight);
    	buffer.writeInt(info.maxHandWeight);
    	buffer.writeInt(info.attackMode.ordinal());
    }
    private static ChrPrivateInfo unpackChrPrivateInfo(ByteBuffer buffer) {
    	if (!buffer.hasRemaining()) return null;
    	int exp = buffer.getInt();
    	int levelUpExp = buffer.getInt();
    	int bagWeight = buffer.getInt();
    	int maxBagWeight = buffer.getInt();
    	int wearWeight = buffer.getInt();
    	int maxWearWeight = buffer.getInt();
    	int handWeight = buffer.getInt();
    	int maxHandWeight = buffer.getInt();
    	int ordinal = buffer.getInt();
    	AttackMode attackMode = AttackMode.All;
    	for (AttackMode item : AttackMode.values()) {
    		if (item.ordinal() == ordinal) {
    			attackMode = item;
    			break;
    		}
    	}
    	return new ChrPrivateInfo(exp, levelUpExp, bagWeight, maxBagWeight, wearWeight, maxWearWeight, handWeight, maxHandWeight, attackMode);
    }
}
