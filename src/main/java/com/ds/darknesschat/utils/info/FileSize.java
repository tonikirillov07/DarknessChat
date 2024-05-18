package com.ds.darknesschat.utils.info;

import com.ds.darknesschat.utils.Utils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record FileSize(String format, double size) {
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "FileSize{" +
                "format='" + format + '\'' +
                ", size=" + size +
                '}';
    }

    public static @NotNull FileSize getMoreComfortableImageSizeFromBytes(long bytesValue){
        String format =  Utils.convertBytesToGigaBytes(bytesValue) >= 1d ? "gigaBytes" : Utils.convertBytesToMegaBytes(bytesValue) >= 1d ? "megaBytes" :
                Utils.convertBytesToKiloBytes(bytesValue) >= 1d ? "kiloBytes" : "bytes";

        double size = Utils.convertBytesToGigaBytes(bytesValue) >= 1d ? Utils.convertBytesToGigaBytes(bytesValue) : Utils.convertBytesToMegaBytes(bytesValue) >= 1d ? Utils.convertBytesToMegaBytes(bytesValue) :
                Utils.convertBytesToKiloBytes(bytesValue) >= 1d ? Utils.convertBytesToKiloBytes(bytesValue) : bytesValue;

        return new FileSize(format, size);
    }
}
