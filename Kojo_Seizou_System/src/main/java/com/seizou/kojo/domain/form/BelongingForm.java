package com.seizou.kojo.domain.form;

import java.util.List;

import lombok.Data;

@Data
public class BelongingForm {
	private String	affilicateId;			// 所属ID
	private String	affilicateName;			// 部署名
	private String	affilicateNameR;		// 部署名略称
	private String	applyStrtDate;			// 適用日(FROM)
	private String	applyFinDate;			// 適用日(TO)
	private List<String> affilicateIdList;	// 削除する所属IDのリスト
}
