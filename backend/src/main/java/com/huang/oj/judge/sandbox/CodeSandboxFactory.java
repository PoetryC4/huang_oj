package com.huang.oj.judge.sandbox;

import com.huang.oj.judge.sandbox.impl.ExampleCodeSandbox;
import com.huang.oj.judge.sandbox.impl.RemoteCodeSandbox;
import com.huang.oj.judge.sandbox.impl.ThirdPartyCodeSandbox;

public class CodeSandboxFactory {
    public static CodeSandbox newInstance(String type) {
        switch (type.toLowerCase()) {
            case "example": {
                return new ExampleCodeSandbox();
            }
            case "remote": {
                return new RemoteCodeSandbox();
            }
            case "thirdparty": {
                return new ThirdPartyCodeSandbox();
            }
            default: {
                return new ExampleCodeSandbox();
            }
        }
    }
}
