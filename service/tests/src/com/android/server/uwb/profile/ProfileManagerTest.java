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

package com.android.server.uwb.profile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import android.content.Context;
import android.os.Handler;
import android.platform.test.annotations.Presubmit;
import android.test.suitebuilder.annotation.SmallTest;

import androidx.test.runner.AndroidJUnit4;

import com.android.server.uwb.UwbConfigStore;
import com.android.server.uwb.UwbInjector;
import com.android.server.uwb.data.ServiceProfileData.ServiceProfileInfo;
import com.android.server.uwb.pm.ProfileManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@SmallTest
@RunWith(AndroidJUnit4.class)
@Presubmit
public class ProfileManagerTest {
    @Mock private Context mContext;
    @Mock private Handler mHandler;
    @Mock private UwbConfigStore mUwbConfigStore;
    @Mock private UwbInjector mUwbInjector;
    private com.android.server.uwb.pm.ProfileManager mProfileManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mProfileManager = new ProfileManager(mContext, mHandler, mUwbConfigStore, mUwbInjector);

    }
    @Test
    public void testAddServiceProfile() {
        assertEquals(0, mProfileManager.mServiceProfileMap.size());

        Optional<UUID> uuid1 = mProfileManager.addServiceProfile(1);
        Optional<UUID> uuid2 = mProfileManager.addServiceProfile(2);

        assertFalse(uuid1.isEmpty());
        assertFalse(uuid2.isEmpty());
        assertEquals(2, mProfileManager.mServiceProfileMap.size());
        assertEquals(1, mProfileManager.mAppServiceProfileMap.size());
    }

    @Test
    public void testRemoveServiceProfile() {
        mProfileManager.addServiceProfile(1);

        assertEquals(1, mProfileManager.mServiceProfileMap.size());
        assertEquals(1, mProfileManager.mAppServiceProfileMap.size());

        for (Map.Entry<UUID, ServiceProfileInfo> entry :
                mProfileManager.mServiceProfileMap.entrySet()) {
            mProfileManager.removeServiceProfile(entry.getKey());
        }

        assertEquals(0, mProfileManager.mServiceProfileMap.size());
    }

    @Test
    public void testLoadServiceProfile() {
        UUID serviceInstanceID = new UUID(100, 50);
        ServiceProfileInfo serviceProfileInfo = new ServiceProfileInfo(serviceInstanceID,
                0, "test", 1);
        serviceProfileInfo.setServiceAdfID(0);
        serviceProfileInfo.setServiceAppletID(1);
        Map<UUID, ServiceProfileInfo> testMap = new HashMap<>();
        testMap.put(serviceProfileInfo.serviceInstanceID, serviceProfileInfo);

        assertEquals(0, mProfileManager.mServiceProfileMap.size());

        mProfileManager.loadServiceProfile(testMap);

        assertEquals(1, mProfileManager.mServiceProfileMap.size());
    }

}