package com.seizou.kojo.domain.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 製品・部材情報 DTO
 * @author K.Tomonari
 */
@Data
@NoArgsConstructor
public class ProductInfoDto {

	private String   itemCd;         // 品番
	private String   itemName;		 // 品名
	private String   itemNameR;	     // 品名略称
	private String   itemDiv;        // 品名区分
	private String   expireDateFrom; // 有効期限日（FROM）
	private String   expireDateTo;   // 有効期限日（TO）
	private Integer  construction;   // 工期
	private List<String> materialId; // 部材ID
	private List<String> reqQty;     // 数量
	private List<String> unit;       // 単位
	private String   message;        // メッセージ
}