package com.sogou.lottery.web.service.qianbao.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sogou.lottery.base.util.MoneyUtil;
import com.sogou.lottery.base.util.SplitUtil;
import com.sogou.lottery.common.exception.BusinessException;

public class PeakTransferResultDto extends PeakCommonResultDto {
	
	private String detail_data;
	
	public List<PeakTransferDetailDto> buildDetails() {
	
		if (StringUtils.isBlank(detail_data)) {
			return new ArrayList<>();
		}
		List<PeakTransferDetailDto> result = new ArrayList<>();
		Iterable<String> details = SplitUtil.on("|").omitEmptyStrings().trimResults().splitToList(detail_data);
		for (String detail : details) {
			Iterable<String> table = SplitUtil.on("^").omitEmptyStrings().trimResults().splitToList(detail);
			int i = 0;
			PeakTransferDetailDto dto = new PeakTransferDetailDto();
			for (String tab : table) {
				if (i == 0) {
					dto.setTransferId(tab);
				} else if (i == 1) {
					dto.setAccountIn(tab);
				} else if (i == 2) {
					dto.setAmount(MoneyUtil.yuan2fen(tab));
				} else {
					dto.setErrorcode(tab);
				}
				i++;
			}
			result.add(dto);
			if (i != 4) {
				throw new BusinessException();
			}
		}
		return result;
	}
	
	public String getDetail_data() {
	
		return detail_data;
	}
	
	public void setDetail_data(String detail_data) {
	
		this.detail_data = detail_data;
	}
	
	public static class PeakTransferDetailDto {
		
		private String transferId;
		private String accountIn;
		private Long amount;
		private String errorcode;
		
		public String getTransferId() {
		
			return transferId;
		}
		
		public void setTransferId(String transferId) {
		
			this.transferId = transferId;
		}
		
		public String getAccountIn() {
		
			return accountIn;
		}
		
		public void setAccountIn(String accountIn) {
		
			this.accountIn = accountIn;
		}
		
		public Long getAmount() {
		
			return amount;
		}
		
		public void setAmount(Long amount) {
		
			this.amount = amount;
		}
		
		public String getErrorcode() {
		
			return errorcode;
		}
		
		public void setErrorcode(String errorcode) {
		
			this.errorcode = errorcode;
		}
		
	}
}
