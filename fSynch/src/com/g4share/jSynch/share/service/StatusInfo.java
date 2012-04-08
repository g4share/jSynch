package com.g4share.jSynch.share.service;

import com.g4share.jSynch.share.ConfigHash;
import com.g4share.jSynch.share.ConfigInfo;

public interface StatusInfo {
    public ConfigHash getConfigHash(ConfigInfo cInfo);
}
