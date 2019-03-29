package Leqitsebi;

//made by @Tiefseetauchner
//automated script to create Router and Switchconfigs for Cisco PT

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MakeScript {
    public static void main(String[] args) {
        try {
            makeRouterScripts("resources/teamsTable.csv", "resources/teams2/");
            makeSwitchScripts("resources/teamsTable.csv", "resources/teams2/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void makeRouterScripts(String inputPath, String outputPath) throws IOException {
        if (!Files.exists(Paths.get(outputPath))) {
            Files.createDirectories(Paths.get(outputPath));
        }
        try (
                BufferedReader in = Files.newBufferedReader(
                        Paths.get(inputPath),
                        Charset.forName("UTF-8")
                )
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.charAt(0) == '#') {
                    continue;
                }
                String[] split = line.split(";");
                int teamNum = Integer.parseInt(split[0]);
                String teamName = split[1];
                String teamKN1 = split[3];
                String teamKN2 = split[5];
                try (
                        BufferedWriter out = Files.newBufferedWriter(
                                Paths.get(outputPath + teamNum + teamName + ".rt.txt"),
                                Charset.forName("UTF-8")
                        )
                ) {
                    out.append("\nen\n" +
                            "conf t\n" +
                            "\n" +
                            "hostname \"R_" + teamName + "\"\n" +
                            "\n" +
                            "enable secret cisco\n" +
                            "service password-encryption\n" +
                            "no ip domain-lookup\n" +
                            "\n" +
                            "banner motd \"Private system! Logoff!!!\"\n" +
                            "\n" +
                            "line con 0\n" +
                            "login\n" +
                            "logging synchronous\n" +
                            "exec-timeout 0 0\n" +
                            "password cisco\n" +
                            "exit\n" +
                            "\n" +
                            "line vty 0 15\n" +
                            "login\n" +
                            "logging synchronous\n" +
                            "exec-timeout 0 0\n" +
                            "password cisco\n" +
                            "exit\n" +
                            "\n" +
                            "int se0/1/0\n" +
                            "no shutdown\n" +
                            "ip address 10.1.2.");
                    if (teamNum == 1) {
                        out.append("56");
                    } else {
                        out.append(((teamNum - 1) * 4 + 1) + "");
                    }
                    out.append(" 255.255.255.252\n" +
                            "\n" +
                            "int se0/1/1\n" +
                            "no shutdown\n" +
                            "ip address 10.1.2.");
                    if (teamNum == 1) {
                        out.append("6 255.255.255.252\n\n" +
                                "int se0/2/0\n" +
                                "no shutdown\n" +
                                "ip address 10.1.2.2 255.255.255.252\n\n"
                        );
                    } else {
                        out.append(((teamNum) * 4 + 2) + " 255.255.255.252\n\n");
                    }
                    out.append("router rip\n" +
                            "no auto-summary\n" +
                            "version 2\n"
                    );
                    if (teamNum == 1) {
                        out.append("network 10.1.2.56\n" +
                                "network 10.1.2.4\n" +
                                "network 10.1.2.0\n"
                        );
                    } else {
                        out.append("network 10.1.2." + ((teamNum) * 4) + "\n" +
                                "network 10.1.2." + ((teamNum - 1) * 4) + "\n"
                        );
                    }
                    out.append("network 192.168." + teamKN1 + ".0\n" +
                            "network 192.168." + teamKN2 + ".0\n" +
                            "network 192.168." + (teamNum + 100) + ".0\n\n" +
                            "int Gig0/0/0\n" +
                            "no shutdown\n\n" +
                            "int Gig0/0/0." + teamKN1 + "\n" +
                            "no shutdown\n" +
                            "encapsulation dot1Q " + teamKN1 + "\n" +
                            "ip address 192.168." + teamKN1 + ".30 255.255.255.224\n" +
                            "helper address 10.1.2.1\n" +
                            "\n" +
                            "int Gig0/0/0." + teamKN2 + "\n" +
                            "no shutdown\n" +
                            "encapsulation dot1Q " + teamKN2 + "\n" +
                            "ip address 192.168." + teamKN2 + ".30 255.255.255.224\n" +
                            "helper address 10.1.2.1\n" +
                            "\n" +
                            "int Gig0/0/0." + (teamNum + 100) + "\n" +
                            "no shutdown\n" +
                            "encapsulation dot1Q " + (teamNum + 100) + " native\n" +
                            "ip address 192.168." + (teamNum + 100) + ".2 255.255.255.224\n" +
                            "helper address 10.1.2.1\n"
                    );
                }
            }
        }
    }

    public static void makeSwitchScripts(String inputPath, String outputPath) throws IOException {
        if (!Files.exists(Paths.get(outputPath))) {
            Files.createDirectories(Paths.get(outputPath));
        }
        try (
                BufferedReader in = Files.newBufferedReader(
                        Paths.get(inputPath),
                        Charset.forName("UTF-8")
                )
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.charAt(0) == '#') {
                    continue;
                }
                String[] split = line.split(";");
                int teamNum = Integer.parseInt(split[0]);
                String teamName = split[1];
                String teamKN1 = split[3];
                String teamKN2 = split[5];
                try (
                        BufferedWriter out = Files.newBufferedWriter(
                                Paths.get(outputPath + teamNum + teamName + ".sw.txt"),
                                Charset.forName("UTF-8")
                        )
                ) {
                    out.append("\n" +
                            "en\n" +
                            "conf t\n" +
                            "\n" +
                            "hostname \"S_" + teamName + "\"\n" +
                            "\n" +
                            "enable secret cisco\n" +
                            "service password-encryption\n" +
                            "no ip domain-lookup\n" +
                            "\n" +
                            "banner motd \"Private system! Logoff!!!\"\n" +
                            "\n" +
                            "line con 0\n" +
                            "login\n" +
                            "logging synchronous\n" +
                            "exec-timeout 0 0\n" +
                            "password cisco\n" +
                            "exit\n" +
                            "\n" +
                            "line vty 0 15\n" +
                            "login\n" +
                            "logging synchronous\n" +
                            "exec-timeout 0 0\n" +
                            "password cisco\n" +
                            "exit\n" +
                            "\n" +
                            "int v" + (100 + teamNum) + "\n" +
                            "no shutdown\n" +
                            "ip address 192.168." + (100 + teamNum) + ".1 255.255.255.252\n" +
                            "\n" +
                            "vlan " + teamKN1 + "\n" +
                            "name " + teamKN1 + "\n" +
                            "\n" +
                            "vlan " + teamKN2 + "\n" +
                            "name " + teamKN2 + "\n" +
                            "\n" +
                            "int gig0/1\n" +
                            "no shutdown\n" +
                            "switchport mode trunk \n" +
                            "switchport trunk allowed vlan none\n" +
                            "switchport trunk allowed vlan " + teamKN1 + "\n" +
                            "switchport trunk allowed vlan " + teamKN2 + "\n" +
                            "switchport trunk allowed vlan " + (100 + teamNum) + "\n" +
                            "switchport trunk native vlan " + (100 + teamNum) + "\n" +
                            "\n" +
                            "int fa0/1\n" +
                            "switchport mode access\n" +
                            "switchport access vlan " + teamKN1 + "\n" +
                            "spanning-tree portfast\n" +
                            "spanning-tree bpduguard enable\n" +
                            "\n" +
                            "int fa0/2\n" +
                            "switchport mode access\n" +
                            "switchport access vlan " + teamKN2 + "\n" +
                            "spanning-tree portfast\n" +
                            "spanning-tree bpduguard enable\n"
                    );
                }
            }
        }
    }
}
