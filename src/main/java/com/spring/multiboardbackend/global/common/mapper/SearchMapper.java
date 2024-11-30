package com.spring.multiboardbackend.global.common.mapper;

import com.spring.multiboardbackend.global.common.request.SearchRequest;
import com.spring.multiboardbackend.global.common.vo.SearchVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SearchMapper {

    @Mapping(target = "offset", expression = "java((request.page() - 1) * request.size())")
    SearchVO toVO(SearchRequest request, Long boardTypeId);

}
