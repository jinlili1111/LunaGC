package emu.grasscutter.server.packet.send;

import com.google.protobuf.CodedOutputStream;
import emu.grasscutter.net.packet.*;
import emu.grasscutter.net.proto.RetcodeOuterClass.Retcode;
import java.io.ByteArrayOutputStream;

public class PacketUnlockTransPointRsp extends BasePacket {
    public PacketUnlockTransPointRsp(Retcode retcode) {
        super(PacketOpcodes.UnlockTransPointRsp);

        try {
            // 6.5.0 client expects UnlockTransPointRsp.retcode on field 7.
            var out = new ByteArrayOutputStream();
            var coded = CodedOutputStream.newInstance(out);
            coded.writeInt32(7, retcode.getNumber());
            coded.flush();
            this.setData(out.toByteArray());
        } catch (Exception ignored) {
            this.setData(new byte[0]);
        }
    }
}
