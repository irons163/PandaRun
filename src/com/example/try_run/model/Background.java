package com.example.try_run.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.try_gameengine.framework.Layer;
import com.example.try_gameengine.framework.Sprite;
import com.example.try_gameengine.stage.StageManager;
import com.example.try_run.R;
import com.example.try_run.utils.CommonUtil;

public class Background extends Layer {
	// 近处的背景
	List<Sprite> arrBG = new ArrayList<Sprite>();
	// 远处的背景
	List<Sprite> arrFar = new ArrayList<Sprite>();

	// public Background {
	// super.init()
	public Background(float x, float y, boolean autoAdd) {
		super(x, y, autoAdd);

		Context context = StageManager.getCurrentStage();
		Bitmap farTexture = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.background_f1);

		Sprite farBg0 = new Sprite(0, 0, false);
		farBg0.setBitmapAndAutoChangeWH(farTexture);
		farBg0.setY(CommonUtil.screenHeight - farTexture.getHeight() - 150);

		Sprite farBg1 = new Sprite(0, 0, false);
		farBg1.setBitmapAndAutoChangeWH(farTexture);
		farBg1.setY(CommonUtil.screenHeight - farTexture.getHeight() - 150);
		farBg1.setX(farBg1.getFrame().width());

		Sprite farBg2 = new Sprite(0, 0, false);
		farBg2.setBitmapAndAutoChangeWH(farTexture);
		farBg2.setY(CommonUtil.screenHeight - farTexture.getHeight() - 150);
		farBg2.setX(farBg2.getFrame().width() * 2);

		this.addChild(farBg0);
		this.addChild(farBg1);
		this.addChild(farBg2);
		arrFar.add(farBg0);
		arrFar.add(farBg1);
		arrFar.add(farBg2);

		Bitmap texture = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.background_f0);
		Sprite bg0 = new Sprite(0, 0, false);
		bg0.setBitmapAndAutoChangeWH(texture);
		bg0.setY(CommonUtil.screenHeight - texture.getHeight());

		Sprite bg1 = new Sprite(0, 0, false);
		bg1.setBitmapAndAutoChangeWH(texture);
		bg1.setY(CommonUtil.screenHeight - texture.getHeight());
		bg1.setX(bg0.getFrame().width());

		this.addChild(bg0);
		this.addChild(bg1);
		arrBG.add(bg0);
		arrBG.add(bg1);
	}

	public void move(float speed) {
		for (Sprite bg : arrBG) {
			bg.setX(bg.getX() - speed);
		}
		if (arrBG.get(0).getX() + arrBG.get(0).getFrame().width() < speed) {
			arrBG.get(0).setX(0);
			arrBG.get(1).setX(arrBG.get(0).getFrame().width());
		}

		for (Sprite far : arrFar) {
			far.setX(far.getX() - speed / 4);
		}
		if (arrFar.get(0).getX() + arrFar.get(0).getFrame().width() < speed / 4) {
			arrFar.get(0).setX(0);
			arrFar.get(1).setX(arrFar.get(0).getFrame().width());
			arrFar.get(2).setX(arrFar.get(0).getFrame().width() * 2);
		}
	}
}
