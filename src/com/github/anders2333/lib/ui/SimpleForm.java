package com.github.anders2333.lib.ui;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;

public class SimpleForm {
	private ArrayList<ElementButton> list = new ArrayList<>();
	private int ID;
	private String Title = "";
	private String Content = "";

	/**
	 * @param ID    表单ID
	 * @param Title 表单标题
	 */
	public SimpleForm(int ID, String Title) {
		this.ID = ID;
		this.Title = Title;
	}

	/**
	 * 
	 * @param ID      表单ID
	 * @param Title   表单标题
	 * @param Content 表单内容
	 */
	public SimpleForm(int ID, String Title, String Content) {
		this.ID = ID;
		this.Title = Title;
		this.Content = Content;
	}

	/**
	 * @param ID 表单ID
	 */
	public SimpleForm(int ID) {
		this.ID = ID;
	}

	public SimpleForm() {
		this.ID = getID();
	}

	/**
	 * 添加一个按钮
	 * 
	 * @param Text 按钮内容
	 * @return
	 */
	public SimpleForm addButton(String Text) {
		list.add(new ElementButton(Text));
		return this;
	}

	/**
	 * 添加一个按钮
	 * 
	 * @param Text    按钮内容
	 * @param isLocal 是否为本地贴图
	 * @param Path    贴图路径
	 * @return
	 */
	public SimpleForm addButton(String Text, boolean isLocal, String Path) {
		list.add(new ElementButton(Text, new ElementButtonImageData(
				isLocal ? ElementButtonImageData.IMAGE_DATA_TYPE_PATH : ElementButtonImageData.IMAGE_DATA_TYPE_URL,
				Path)));
		return this;
	}

	/**
	 * 设置表单标题
	 * 
	 * @param Title
	 * @return
	 */
	public SimpleForm setTitle(String Title) {
		this.Title = Title;
		return this;
	}

	/**
	 * 设置表单ID
	 * 
	 * @param ID
	 * @return
	 */
	public SimpleForm setID(int ID) {
		this.ID = ID;
		return this;
	}

	private int getID() {
		int length = getRand(1, 5);
		String ID = "";
		for (int i = 0; i < length; i++)
			ID += getRand(0, 9);
		return Integer.valueOf(ID);
	}

	private int getRand(int min, int max) {
		return (int) (min + Math.random() * (max - min + 1));
	}

	/**
	 * 设置表单内容
	 * 
	 * @param Content
	 * @return
	 */
	public SimpleForm setContent(String Content) {
		this.Content = Content;
		return this;
	}

	/**
	 * 将表单发送给指定玩家列表
	 * 
	 * @param players
	 * @return
	 */
	public int sendPlayers(List<Player> players) {
		for (Player player : players)
			player.showFormWindow(new FormWindowSimple(Title, Content, list), ID);
		return ID;
	}

	/**
	 * 将表单发送给指定玩家
	 * 
	 * @param player
	 * @return 表单ID
	 */
	public int sendPlayer(Player player) {
		player.showFormWindow(new FormWindowSimple(Title, Content, list), ID);
		return ID;
	}
}
