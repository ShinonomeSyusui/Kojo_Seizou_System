function formClear() {
  	document.getElementById('itemCd').value='';
    document.getElementById('itemName').value='';
    document.getElementById('itemNameR').value='';
    document.getElementById('itemDiv1').value='';
    document.getElementById('itemDiv2').value='';
    document.getElementById('itemDiv3').value='';
    document.getElementById('expireDateFrom').value='';
    document.getElementById('expireDateTo').value='';
    document.getElementById('construction').value='';
}

document.getElementById('clear').addEventListener('click', formClear, false);

function elementDisabled(e) {

   // 更新画面の場合
   if(value === 'bfmk06'){
		if(e.target.id != itemValue){
			document.getElementById(itemValue).checked = true;
		}
	}
	
   	// 品名区分が部材の場合
   	if (document.getElementById('itemDiv3').checked) {
	 	document.getElementById('itemNameR').disabled = true;
	 	document.getElementById('construction').disabled = true;
	 	document.getElementById('deleteRow').disabled = true;
	 	document.getElementById('addRow').disabled = true;
	
		for(input of document.querySelectorAll('#componentTable input')) {
			input.disabled = true;
			
		}

  	} else {
		document.getElementById('itemNameR').disabled = false;
		document.getElementById('construction').disabled = false;
		document.getElementById('deleteRow').disabled = false;
		document.getElementById('addRow').disabled = false;
	
		for(input of document.querySelectorAll('#componentTable input')) {
			input.disabled = false;
		}
		
  	}
  	
  	// 参照権限・操作権限がない場合
  	if(document.getElementById('message').value === '参照権限・操作権限がありません。') {
	  	document.getElementById('itemCd').disabled = true;
    	document.getElementById('itemName').disabled = true;
    	document.getElementById('itemNameR').disabled = true;
    	document.getElementById('itemDiv1').disabled = true;
    	document.getElementById('itemDiv2').disabled = true;
    	document.getElementById('itemDiv3').disabled = true;
    	document.getElementById('expireDateFrom').disabled = true;
    	document.getElementById('expireDateTo').disabled = true;
    	document.getElementById('construction').disabled = true;
		document.getElementById('itemNameR').disabled = true;
		document.getElementById('construction').disabled = true;
		document.getElementById('deleteRow').disabled = true;
		document.getElementById('addRow').disabled = true;
		document.getElementById('register').disabled = true;
		document.getElementById('clear').disabled = true;
    	for(input of document.querySelectorAll('#componentTable input')) {
		     input.disabled = true;	
		}
}	
  	
}

window.onload = elementDisabled;
document.getElementById('itemCd').focus();


const tmpDispId = document.getElementById('tmpDispId');
const value = tmpDispId.getAttribute('value');
if(value === 'bfmk06'){
	document.getElementById('itemCd').readOnly = true;
	document.getElementById('itemName').readOnly = true;
	document.getElementById('expireDateFrom').readOnly = true;
}

const itemDiv = document.getElementsByName('itemDiv');
let itemValue = "";
for(item of itemDiv) {
	if(item.checked === true){
		 itemValue = "itemDiv" + item.value;
		
	}
	item.addEventListener('click', elementDisabled, false);
}
