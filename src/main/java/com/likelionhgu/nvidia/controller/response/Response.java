package com.likelionhgu.nvidia.controller.response;
import com.likelionhgu.nvidia.dto.RecommendDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Response {
    private List<RecommendDto> recommendList;

}