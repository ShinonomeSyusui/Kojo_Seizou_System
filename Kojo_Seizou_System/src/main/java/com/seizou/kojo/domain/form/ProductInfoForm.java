package com.seizou.kojo.domain.form;


import java.util.List;

import lombok.Data;

/**
 * 製品・部材情報のFormクラス
 * @author K.Tomonari
 */
@Data
public class ProductInfoForm {
	
	private String   itemCd;         // 品番
	private String   itemName;		 // 品名
	private String   itemNameR;	     // 品名略称
	private String   itemDiv;        // 品名区分
	private String   expireDateFrom; // 有効期限日（FROM）
	private String   expireDateTo;   // 有効期限日（TO）
	private Integer  construction;   // 工期
	private List<ComponentInfoForm> componentList; // 部材
	private String tmpDispId;	     // 遷移前画面ID
}