/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.uwb.secure.csml;

import static com.google.common.truth.Truth.assertThat;

import com.android.server.uwb.secure.iso7816.TlvDatum;
import com.android.server.uwb.util.DataTypeConversionUtil;

import org.junit.Test;

/**
 * Tests for PutDoCommand.
 */
public class PutDoCommandTest {

    @Test
    public void encodePutDoCommand() {
        TlvDatum.Tag doTag = new TlvDatum.Tag(DataTypeConversionUtil.hexStringToByteArray("0A0B"));
        byte[] doData = DataTypeConversionUtil.hexStringToByteArray("A0B0");
        // <code>cla | ins | p1 | p2 | lc | data | le</code>
        byte[] expectedApdu = DataTypeConversionUtil.hexStringToByteArray(
                "00DB3FFF050A0B02A0B000");
        byte[] actualApdu = PutDoCommand.build(new TlvDatum(doTag, doData))
                .getCommandApdu().getEncoded();

        assertThat(actualApdu).isEqualTo(expectedApdu);
    }
}
