function formClear() {
  document.getElementById("affilicateId").value='';
  document.getElementById("userId").value='';
  document.getElementById("userName").value='';
  document.getElementById("auth_div_guest").value='';
  document.getElementById("auth_div_general").value='';
  document.getElementById("auth_div_manager").value='';
  document.getElementById("expireDateFrom").value='';
  document.getElementById("expireDateTo").value='';
}

function confirmDelete () {
	const result = window.confirm("削除してもよろしいですか？"); 
		
	if(result) {
		const form = document.forms[0];
		
		form.action = '/b-forme_Kojo/pc/202/delete';
		form.submit();
	
	}

}

document.getElementById("clear").addEventListener('click', formClear, false);

if(document.getElementById("delete") !== null) {
	document.getElementById("delete").addEventListener('click', confirmDelete, false);
}

if(document.getElementById('authDiv').value === '1' 
 || document.getElementById('authDiv').value === '2' ) {
	document.getElementById('affilicateId').disabled = true;
	document.getElementById('userId').disabled = true;
	document.getElementById('userName').disabled = true;
	document.getElementById('userName').disabled = true;
	for(it of document.getElementsByName('authDiv')) {
		it.disabled = true;
	}
	
	document.getElementById('expireDateFrom').disabled = true;
	document.getElementById('expireDateTo').disabled = true;
	document.getElementById('search').disabled = true;
	document.getElementById('clear').disabled = true;
}

if(document.getElementById('authDiv').value === '3') {
	document.getElementById('affilicateId').readOnly = true;
}

