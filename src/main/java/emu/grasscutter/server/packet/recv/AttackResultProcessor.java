package emu.grasscutter.server.packet.recv;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.net.proto.AttackResultOuterClass.AttackResult;

final class AttackResultProcessor {
    private AttackResultProcessor() {}

    static int getDefenseId(AttackResult result) {
        int defenseId = result.getDefenseId();
        if (defenseId != 0) {
            return defenseId;
        }

        var field = result.getUnknownFields().getField(5);
        if (field != null && !field.getVarintList().isEmpty()) {
            return field.getVarintList().get(0).intValue();
        }

        return 0;
    }

    static void flush(Player player) {
        while (!player.getAttackResults().isEmpty()) {
            AttackResult result = player.getAttackResults().poll();
            int defenseId = getDefenseId(result);
            Grasscutter.getLogger()
                    .info(
                            "[CombatHP] flush attack attacker={} target={} damage={} element={}",
                            result.getAttackerId(),
                            defenseId,
                            result.getDamage(),
                            result.getElementType());
            player.getScene().handleAttack(result);
        }
    }
}
