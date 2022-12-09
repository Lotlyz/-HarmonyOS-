/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.lotday2.utils;

import ohos.agp.components.Attr;
import ohos.agp.components.AttrSet;

import java.util.NoSuchElementException;

public class TypedAttrUtils {
    private TypedAttrUtils() {
    }

    /**
     * 是否开启
     *
     * @param attrs 属性集
     * @param attrName 值
     * @param isDefValue 值
     * @return true是，反之
     */
    public static boolean getBoolean(AttrSet attrs, String attrName, boolean isDefValue) {
        Attr attr = attrNoSuchElement(attrs, attrName);
        if (attr == null) {
            return isDefValue;
        } else {
            return attr.getBoolValue();
        }
    }

    /**
     * int值
     *
     * @param attrs 属性集
     * @param attrName 属性
     * @param defValue 值
     * @return 对应int值
     */
    public static int getInt(AttrSet attrs, String attrName, int defValue) {
        Attr attr = attrNoSuchElement(attrs, attrName);
        if (attr == null) {
            return defValue;
        } else {
            return attr.getIntegerValue();
        }
    }

    /**
     * string 类型
     *
     * @param attrs 属性集
     * @param attrName 值
     * @param defValue 值
     * @return 信息
     */
    public static String getString(AttrSet attrs, String attrName, String defValue) {
        Attr attr = attrNoSuchElement(attrs, attrName);
        if (attr == null) {
            return defValue;
        } else {
            return attr.getStringValue();
        }
    }

    /**
     * 属性处理
     *
     * @param attrs 属性集
     * @param attrName 属性
     * @return 对应值
     */
    private static Attr attrNoSuchElement(AttrSet attrs, String attrName) {
        Attr attr = null;
        try {
            attr = attrs.getAttr(attrName).get();
        } catch (NoSuchElementException e) {
            e.fillInStackTrace();
        }
        return attr;
    }
}
