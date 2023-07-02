package com.netby.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: byg
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class PairVO<L, R> extends BaseVO {
    L left;
    R right;
}
