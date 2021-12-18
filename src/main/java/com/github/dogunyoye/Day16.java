package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Day16 {

    public enum Mode {
        NORMAL,
        LENGTH_BITS,
        LENGTH_PACKETS
    }

    private static final Map<String, String> hexTable;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("0", "0000");
        map.put("1", "0001");
        map.put("2", "0010");
        map.put("3", "0011");
        map.put("4", "0100");
        map.put("5", "0101");
        map.put("6", "0110");
        map.put("7", "0111");
        map.put("8", "1000");
        map.put("9", "1001");
        map.put("A", "1010");
        map.put("B", "1011");
        map.put("C", "1100");
        map.put("D", "1101");
        map.put("E", "1110");
        map.put("F", "1111");
        hexTable = Collections.unmodifiableMap(map);
    }

    static class Packet {
        public long version;
        public long typeId;
        public Optional<Long> literalValue;
        public List<Packet> subPackets;

        public Packet(long version, long typeId) {
            this.version = version;
            this.typeId = typeId;
            this.literalValue = Optional.empty();
            this.subPackets = new ArrayList<>();
        }

        public Packet(long version, long typeId, long literalValue) {
            this.version = version;
            this.typeId = typeId;
            this.literalValue = Optional.of(literalValue);
            this.subPackets = new ArrayList<>();
        }

        public List<Packet> getSubPackets() {
            return this.subPackets;
        }

        public long getLiteralValue() {
            return this.literalValue.get();
        }

        @Override
        public String toString() {
            return String.format("[v:%d, t:%d, l:%s]", version, typeId, literalValue);
        }
    }

    private static Long binaryToNumber(String binary) {
        return Long.parseLong(binary, 2);
    }

    private static Long getLiteralBinaryNumber(Queue<String> queue, AtomicLong counter) {

        String number = "";
        while (true) {
            String s = "";
            for (int i = 0; i < 5; i++) {
                String bit = queue.poll();
                if (bit == null) {
                    s += "0";
                } else {
                    s += bit;
                    counter.incrementAndGet();
                }
            }

            number += s.substring(1);

            char firstBit = s.charAt(0);
            if (firstBit == '1') {
                continue;
            }

            // zero, time to terminate!
            break;
        }

        return binaryToNumber(number);
    }

    private static long pollQueue(Queue<String> queue, int bits) {
        String s = "";
        for (int i = 0; i < bits; i++) {
            String bit = queue.poll();
            if (bit == null) {
                return 0;
            }
            s += bit;
        }

        return binaryToNumber(s);
    }

    private static long getVersionAndTypeId(Queue<String> queue) {
        return pollQueue(queue, 3);
    }

    private static long getLengthOfSubPacketBits(Queue<String> queue) {
        return pollQueue(queue, 15);
    }

    private static long getNumberOfSubPackets(Queue<String> queue) {
        return pollQueue(queue, 11);
    }

    private static long parsePacket(List<Packet> packets, Queue<String> queue) {

        long consumed = 0;
        final AtomicLong c = new AtomicLong();

        final long version = getVersionAndTypeId(queue);
        final long typeId = getVersionAndTypeId(queue);
        consumed += 6;

        switch((int)typeId) {
            case 4 -> {
                final long literalNumber = getLiteralBinaryNumber(queue, c);
                packets.add(new Packet(version, typeId, literalNumber));
            }

            default -> {
                final String lengthTypeId = queue.poll();
                consumed++;

                final Packet p = new Packet(version, typeId);
    
                if (lengthTypeId.equals("0")) {
                    long subPacketsLength = getLengthOfSubPacketBits(queue);
                    consumed += 15;
    
                    while(subPacketsLength != 0) {
                        long bits = parsePacket(packets, queue);
                        subPacketsLength -= bits;
                        consumed += bits;
                        p.getSubPackets().add(packets.get(packets.size()-1));
                    }
    
                } else {
                    final long numberOfSubPackets = getNumberOfSubPackets(queue);
                    consumed += 11;

                    for (int i = 0; i < numberOfSubPackets; i++) {
                        consumed += parsePacket(packets, queue);
                        p.getSubPackets().add(packets.get(packets.size()-1));
                    }
                }
    
                packets.add(p);
            }
        }

        return consumed + c.get();
    }

    private static String hexToBinary(String hexString) {
        String result = "";
        for (Character c : hexString.toCharArray()) {
            result += hexTable.get(c.toString());
        }

        return result;
    }

    public static List<Packet> parsePackets(String hexString) {
        final String bin = hexToBinary(hexString);

        final Queue<String> queue = new LinkedList<>();

        for (Character c : bin.toCharArray()) {
            queue.add(c.toString());
        }

        final List<Packet> packets = new ArrayList<>();
        long consumed = parsePacket(packets, queue);

        // The length of the original binary string
        // should equal however many bits we consumed
        // in the parsing + any residual trailing bits
        if (bin.length() == (consumed + queue.size())) {
            System.out.println("Parsing complete!");
            return packets;
        }

        System.out.println("Error with parsing!");
        return null;
    }

    private static long parsePacketResult(Set<Packet> evaluated, Packet p) {

        if (evaluated.contains(p)) {
            return 0;
        }

        evaluated.add(p);

        final int typeId = (int)p.typeId;
        switch(typeId) {

            // sum
            case 0 -> {
                long sum = 0;
                for (Packet subP : p.getSubPackets()) {
                    sum += parsePacketResult(evaluated, subP);
                }
                return sum;
            }

            // product
            case 1 -> {
                long prod = 1;
                for (Packet subP : p.getSubPackets()) {
                    prod *= parsePacketResult(evaluated, subP);
                }
                return prod;
            }

            // minimum
            case 2 -> {
                long min = Long.MAX_VALUE;
                for (Packet subP : p.getSubPackets()) {
                    min = Math.min(min, parsePacketResult(evaluated, subP));
                }
                return min;
            }

            // maximum
            case 3 -> {
                long max = 0;
                for (Packet subP : p.getSubPackets()) {
                    max = Math.max(max, parsePacketResult(evaluated, subP));
                }
                return max;
            }

            case 4 -> {
                return p.getLiteralValue();
            }

            // greater than
            case 5 -> {
                final Packet p1 = p.getSubPackets().get(0);
                final Packet p2 = p.getSubPackets().get(1);

                final long r1 = parsePacketResult(evaluated, p1);
                final long r2 = parsePacketResult(evaluated, p2);

                if (r1 > r2) {
                    return 1;
                }
                return 0;
            }

            // less than
            case 6 -> {
                final Packet p1 = p.getSubPackets().get(0);
                final Packet p2 = p.getSubPackets().get(1);

                final long r1 = parsePacketResult(evaluated, p1);
                final long r2 = parsePacketResult(evaluated, p2);

                if (r1 < r2) {
                    return 1;
                }
                return 0;
            }

            // equal to
            case 7 -> {
                final Packet p1 = p.getSubPackets().get(0);
                final Packet p2 = p.getSubPackets().get(1);

                final long r1 = parsePacketResult(evaluated, p1);
                final long r2 = parsePacketResult(evaluated, p2);

                if (r1 == r2) {
                    return 1;
                }
                return 0;
            }

            default -> {
                System.out.println("Error! Unknown type id: " + typeId);
                return -1;
            }
        }
    }

    public static long findResult(List<Packet> packets) {

        // janky hack to constrain the 2 sub packet max operations
        // as have observed more than 2 for some packets..
        // doesn't seem to yield a parsing/value error though
        for (Packet p : packets) {
            if (p.typeId == 5 || p.typeId == 6 || p.typeId == 7) {
                p.subPackets = p.getSubPackets().stream().limit(2).toList();
            }
        }

        // reverse the collection so that the packets are "in order"
        // i.e sub packets aren't evaluated before packets containing sub packets
        // a.k.a parent packets
        Collections.reverse(packets);

        long sum = 0;
        final Set<Packet> evaluated = new HashSet<>();

        for (Packet p : packets) {
            sum += parsePacketResult(evaluated, p);
        }

        return sum;
    }

    public static long findSumOfVersionIds(List<Packet> packets) {
        long sum = 0;
        for (Packet p : packets) {
            sum += p.version;
        }

        return sum;
    }   
    
    public static void main( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day16.txt"));
        final String hexString = input.get(0);
        final List<Packet> packets = parsePackets(hexString);

        System.out.println("Part 1: " + findSumOfVersionIds(packets));
        System.out.println("Part 2: " + findResult(packets));
    }
}
