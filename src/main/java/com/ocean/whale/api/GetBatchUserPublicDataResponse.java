package com.ocean.whale.api;

import com.ocean.whale.model.UserPublicData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetBatchUserPublicDataResponse {
    List<UserPublicData> userPublicDataList;
}
