package com.seizou.kojo.domain.dao;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 製品・部材情報 Dao
 * @author K.Tomonari
 */
@Data
@NoArgsConstructor
public class ProductInfoDao {

	private String   facCd;          // 工場CD
	private String   itemCd;         // 品番
	private String   itemName;		 // 品名
	private String   itemNameR;	     // 品名略称
	private String   itemDiv;        // 品名区分
	private String   expireDateFrom; // 有効期限日（FROM）
	private String   expireDateTo;   // 有効期限日（TO）
	private Integer  construction;   // 工期
	private Integer  oprConQty;      // 作業工程数
	private List<String> manuId;     // 製品ID
	private List<String> materialId; // 部材ID
	private List<BigDecimal> reqQty; // 数量
	private List<String> unit;       // 単位
	private String   message;        // メッセージ
	private String   userId;		 // ユーザーID
}