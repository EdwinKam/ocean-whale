package com.ocean.whale.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetBatchUserPublicDataRequest {
    List<String> userIds;
}
