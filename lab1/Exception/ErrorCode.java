package lab1.Exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode extends RuntimeException {
    public static final int IO_EXCEPTION = 1;
    public static final int CLASS_NOT_FOUND = 2;
    public static final int MD5_FAILED = 3;
    public static final int CHECKSUM_CHECK_FAILED = 4;
    public static final int INVALID_CURSOR = 5;
    public static final int WRONG_PARAM = 6;
    public static final int FILE_NOT_FOUND = 7;
    public static final int FILE_ALREADY_EXISTS = 8;
    public static final int EOF = 9;
    public static final int BLOCK_NOT_FOUND = 10;
    public static final int BLOCK_BROKEN = 11;
    public static final int INDEX_OUT_OF_BOUND = 12;
    public static final int SYSTEM_ERROR = 13;

    // ... and more
    public static final int UNKNOWN = 1000;

    private static final Map<Integer, String> ErrorCodeMap = new HashMap<>();

    static {
        ErrorCodeMap.put(IO_EXCEPTION, "IO exception");
        ErrorCodeMap.put(CLASS_NOT_FOUND, "class not found");
        ErrorCodeMap.put(MD5_FAILED, "md5: no such algorithm");
        ErrorCodeMap.put(CHECKSUM_CHECK_FAILED, "block checksum check failed");
        ErrorCodeMap.put(INVALID_CURSOR, "file cursor move invalid");
        ErrorCodeMap.put(WRONG_PARAM, "The parameter is wrong");
        ErrorCodeMap.put(FILE_NOT_FOUND, "file does not exist");
        ErrorCodeMap.put(FILE_ALREADY_EXISTS, "file already exists");
        ErrorCodeMap.put(EOF, "End of File");
        ErrorCodeMap.put(BLOCK_NOT_FOUND, "block does not exist");
        ErrorCodeMap.put(BLOCK_BROKEN, "block is broken");
        ErrorCodeMap.put(INDEX_OUT_OF_BOUND, "The index is out of bound");
        ErrorCodeMap.put(SYSTEM_ERROR, "system error");

        ErrorCodeMap.put(UNKNOWN, "unknown");
    }

    public static String getErrorText(int errorCode) {
        return ErrorCodeMap.getOrDefault(errorCode, "invalid");
    }

    private int errorCode;

    public ErrorCode(int errorCode) {
        super(String.format("error code '%d' \"%s\"", errorCode, getErrorText(errorCode)));
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    } }
