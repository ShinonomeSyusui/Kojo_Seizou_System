package com.seizou.kojo.domain.form;


import lombok.Data;

/**
 * 製品・部材情報のFormクラス
 * @author K.Tomonari
 */
@Data
public class ComponentInfoForm {
	
	private Boolean deleteFlg; // 削除フラグ
	private String materialId; // 部材ID
	private String reqQty;     // 数量
	private String unit;       // 単位
}