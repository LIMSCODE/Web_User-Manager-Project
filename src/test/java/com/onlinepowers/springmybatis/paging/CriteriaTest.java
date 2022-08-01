package com.onlinepowers.springmybatis.paging;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class CriteriaTest {

	@Test
	public void getId(){

		Criteria criteria = new Criteria();
		criteria.setCurrentPageNo(1);
		criteria.setRecordsPerPage(2);

		PaginationInfo paginationInfo = new PaginationInfo(criteria);
		paginationInfo.setTotalRecordCount(4);

		criteria.setPaginationInfo(paginationInfo);
		assertThat(criteria.getStartPage()).isEqualTo(0);

	}



}
