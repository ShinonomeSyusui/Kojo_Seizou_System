//package com.seizou.kojo.domain.controller;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.ResourceBundle;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.seizou.kojo.domain.dto.CommonDto;
//import com.seizou.kojo.domain.dto.ProductInfoDto;
//import com.seizou.kojo.domain.form.ComponentInfoForm;
//import com.seizou.kojo.domain.form.ProductInfoForm;
//import com.seizou.kojo.domain.service.Bfmk05Service;
//
///**
//* 製品・部材情報 Controller
//* @author K.Tomonari
//*/
//@Controller
//@RequestMapping("/b-forme_Kojo")
//public class Bfmk05Controller {
//
//    @Autowired
//    Bfmk05Service service;
//    
//	// メッセージ取得用の変数を宣言
//	private static final String MESSAGES_NAME = "resources_ja";
//	private static ResourceBundle mes = ResourceBundle.getBundle(MESSAGES_NAME);
//    
//	//初期件数
//	private final int INIT_NUM = 5;
//	
//	/**
//	 * 初期画面
//	 * @param form
//	 * @param model
//	 * @param itemCd
//	 * @return 画面名
//	 */
//	@GetMapping("/pc/205")
//	public String init(@ModelAttribute ProductInfoForm form, Model model, 
//			@RequestParam(name = "itemCd", required = false) String itemCd) {
//		
//		// 共通DTO
//		CommonDto commonDto = new CommonDto(null, null, "bfmk05", null, "bfm1", "su1", null, "itns001", null);
//		// 出力用の部材レコード
//		List<ComponentInfoForm> componentList = new ArrayList<ComponentInfoForm>();
//		
//		// 初期化処理
//		ProductInfoDto productInfoDto = service.init(commonDto, itemCd);
//		
//		// 遷移元が製品一覧画面の場合
//		if(productInfoDto.getMessage() == null 
//			&& commonDto.getTmpDispId().equals("bfmk06")) {
//				
//			form.setItemCd(productInfoDto.getItemCd());                 // 品番
//			form.setItemName(productInfoDto.getItemName());             // 品名
//			form.setItemNameR(productInfoDto.getItemNameR());           // 品名略称
//			form.setItemDiv(productInfoDto.getItemDiv());               // 品名区分
//			form.setExpireDateFrom(productInfoDto.getExpireDateFrom()); // 有効期限日（FROM）
//			form.setExpireDateTo(productInfoDto.getExpireDateTo());     // 有効期限日（TO）
//			form.setConstruction(productInfoDto.getConstruction());     // 工期
//			
//			for(int i = 0; i < productInfoDto.getMaterialId().size(); i++) {
//				ComponentInfoForm comForm = new ComponentInfoForm();
//				comForm.setMaterialId(productInfoDto.getMaterialId().get(i)); // 製品ID
//				comForm.setReqQty(productInfoDto.getReqQty().get(i));         // 数量
//				comForm.setUnit(productInfoDto.getUnit().get(i));             // 単位
//				componentList.add(comForm);
//			}
//
//			// 製品区分が部材の場合
//			if(productInfoDto.getMaterialId().size() == 0) {
//				// 5レコードを部材の初期表示
//				for(int i = 0; i < INIT_NUM; i++) {
//					componentList.add(new ComponentInfoForm());
//					form.setComponentList(componentList);
//				}
//			}
//			form.setComponentList(componentList);                       // 部材
//			
//		} else {
//			// 5レコードを部材の初期表示
//			for(int i = 0; i < INIT_NUM; i++) {
//				componentList.add(new ComponentInfoForm());
//				form.setComponentList(componentList);
//			}
//			form.setItemDiv("1");
//		
//		}
//		
//		// 遷移前画面IDを設定
//		form.setTmpDispId(commonDto.getTmpDispId());
//		model.addAttribute("dispId" , form.getTmpDispId());
//		
//		// メッセージを設定
//		if(productInfoDto.getMessage() != null) {
//			String message = mes.getString(productInfoDto.getMessage());
//			model.addAttribute("message" , message);
//			// メッセージの文字色の設定
//			model.addAttribute("successFlg" , false);
//		}
//		
//		return "bfmk05View";
//	}
//	
//	/**
//	 * メニュー画面に移動
//	 * @param model
//	 * @return 画面名
//	 */
//	@GetMapping("/pc/205/back")
//	public String back(Model model) {
//		return "bfkt02View";
//	}
//	
//	/**
//	 * クリア処理
//	 * @param form
//	 * @param model
//	 * @param itemCd
//	 * @return 画面名
//	 */
//	@PostMapping("/pc/205/clear")
//	public String clear(@ModelAttribute ProductInfoForm form, Model model,
//			@RequestParam(name = "itemCd", required = false) String itemCd) {
//		
//		// 遷移元が製品一覧画面の場合
//		if(form.getTmpDispId().equals("bfmk06")) {
//			itemCd = itemCd.substring(0, itemCd.length() - 1);
//		}
//
//		// 初期画面処理
//		init(form, model, itemCd);
//		
//		return "bfmk05View";
//	}
//	
//	/**
//	 * 登録処理
//	 * @param form
//	 * @param model
//	 * @return 画面名
//	 */
//	@PostMapping("/pc/205/register")
//	public String register(@ModelAttribute ProductInfoForm form, Model model) {
//		
//		// 登録処理を実行
//		ProductInfoDto productInfoDto = service.register(form);
//		
//		// メッセージを設定
//		if(productInfoDto.getMessage() != null) {
//			String message = mes.getString(productInfoDto.getMessage());
//			model.addAttribute("message" , message);
//		
//			// メッセージの文字色を設定
//			if(productInfoDto.getMessage().equals("mszemd016")) {
//				model.addAttribute("successFlg" , true);
//			} else {
//				model.addAttribute("successFlg" , false);
//			}
//		}
//		
//		// 製品区分が部材の場合
//		if(form.getItemDiv().equals("3")) {
//			List<ComponentInfoForm> componentList = new ArrayList<ComponentInfoForm>();
//			// 5レコードを部材の初期表示
//			for(int i = 0; i < INIT_NUM; i++) {
//				componentList.add(new ComponentInfoForm());
//				form.setComponentList(componentList);
//			}
//		}
//		
//		// 遷移前画面IDを設定
//		model.addAttribute("dispId" , form.getTmpDispId());
//		
//		return "bfmk05View";
//	}
//	
//	/**
//	 * 更新処理
//	 * @param form
//	 * @param model
//	 * @return 画面名
//	 */
//	@PostMapping("/pc/205/update")
//	public String update(@ModelAttribute ProductInfoForm form, Model model) {
//		
//		// 登録処理を実行
//		ProductInfoDto productInfoDto = service.update(form);
//		
//		// メッセージを設定
//		if(productInfoDto.getMessage() != null) {
//			String message = mes.getString(productInfoDto.getMessage());
//			model.addAttribute("message" , message);
//			// メッセージの文字色を設定
//			if(productInfoDto.getMessage().equals("mszemd017")) {
//				model.addAttribute("successFlg" , true);
//			} else {
//				model.addAttribute("successFlg" , false);
//			}
//		}
//		
//		// 製品区分が部材の場合
//		if(form.getItemDiv().equals("3")) {
//			List<ComponentInfoForm> componentList = new ArrayList<ComponentInfoForm>();
//			// 5レコードを部材の初期表示
//			for(int i = 0; i < INIT_NUM; i++) {
//				componentList.add(new ComponentInfoForm());
//				form.setComponentList(componentList);
//			}
//		}
//		
//		// 遷移前画面IDを設定
//		model.addAttribute("dispId" , form.getTmpDispId());
//		
//		return "bfmk05View";
//	}
//	
//	/**
//	 * 部材レコード追加
//	 * @param form
//	 * @param model
//	 * @return 画面名
//	 */
//	@PostMapping("/pc/205/addRow")
//	public String addRow(@ModelAttribute ProductInfoForm form, 
//			Model model) {
//		
//		// レコードを追加
//		List<ComponentInfoForm> componentList = form.getComponentList();
//		componentList.add(new ComponentInfoForm());
//		form.setComponentList(componentList);
//		
//		// 遷移前画面IDを設定
//		model.addAttribute("dispId" , form.getTmpDispId());
//		
//		return "bfmk05View";
//	}
//	
//	/**
//	 * 部材レコード削除
//	 * @param form
//	 * @param model
//	 * @return 画面名
//	 */
//	@PostMapping("/pc/205/deleteRow")
//	public String deleteRow(@ModelAttribute ProductInfoForm form, 
//			Model model) {
//		
//		// 出力用の部材レコード
//		List<ComponentInfoForm> componentList = new ArrayList<ComponentInfoForm>();
//		// 元の部材レコード
//		List<ComponentInfoForm> comFormList = form.getComponentList();
//		
//		// レコードが1つだけの場合
//		if(comFormList.size() == 1) {
//			componentList.add(comFormList.get(0));
//		} else {
//			for(ComponentInfoForm com : comFormList) {
//				// 削除フラグがTrueの場合
//				if(!com.getDeleteFlg()) {
//					componentList.add(com);
//				}
//			}
//		}
//		
//		// 全レコード削除された場合
//		if(componentList.isEmpty()) {
//			componentList.add(new ComponentInfoForm());
//		}
//		
//		// 出力レコードをformに追加
//		form.setComponentList(componentList);
//		
//		// 遷移前画面IDを設定
//		model.addAttribute("dispId" , form.getTmpDispId());
//		
//		return "bfmk05View";
//	}
//}
