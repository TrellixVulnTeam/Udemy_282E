package com.telimay.spring.boot.backend.apirest.auth;

public class JwtConfig {
	
	public static final String RSA_PRIVATE ="-----BEGIN RSA PRIVATE KEY-----\r\n"
			+ "MIIEogIBAAKCAQEA9fh6UtrIDe5Fq49M98i4tZtKED3vmBxxB3Kx+2mvuJ4lu4Wd\r\n"
			+ "00LF7XGyAUoK8wpGEdoJC0Fk7aSyCURvbgggydZucJGtipDBWSnpoHdKsYQJcz0I\r\n"
			+ "tvxWNYENwYLrIPke5BpxWRN6Mc6BN8RWjtAxxfuR1BXFinHgGt3JDZu4wSmv+9g7\r\n"
			+ "SRXnwGsgy7Ji0b2TBGbExyt2fWZtleR6Eb8vMPu1qtvHItvlQqV6NYti4qg6xezY\r\n"
			+ "Oet+sREE4WNoM79cv04RNCqbja+6eYJmtJnGvqMmQM/Q0jMTtCZBROdPNsOuaKRZ\r\n"
			+ "Cyl+kL/BBy3kBdzuxSEp+pAzD9Zvuw8LWNZy2QIDAQABAoIBAERL0wMwdj3P/A5P\r\n"
			+ "wspGNGtQi/cIi39L3mJ4CU1Kohz75HQ1/IWNU9HDMO7EtVeYca5BQSOb679EbJ7A\r\n"
			+ "HsSJgzknzhB5kpsX6GeuiI0isl2p0Mnrl4DffbxrcftaM5k7MsWZRgG6s8IimaR3\r\n"
			+ "YqVSz48uc+ZUiT4Qsm4jSND7fQg81KIUk0/VG1US4zZsv3iH0mPLEgWK9x3kvdQj\r\n"
			+ "hX5viUO7RsQhmeJ1oT6l/jHpU5lSGLphwgTzdwQegd++GSvI/jscApId3XtlJZDa\r\n"
			+ "XNuamstYipPYE5ElHu3sJRiViNvw72E9i4rcX7pHn6LuMBOcaod4k4qbItiU5hde\r\n"
			+ "DDOIb1ECgYEA/Sv0r7hxjycrBdyUjw8tVcIWPDv8Z4BC97hxLCOGe7A6KvT1JpL2\r\n"
			+ "J2EGXLQVL4bj2Vutr+pCL7cZfFfibwB0hEJ2j693CNKsw/5bsmouwHyj8mHjzyDy\r\n"
			+ "ZfLKiJTrwr9e4ynGGNOuv8eEWyt5ALW6GWfVzTFaJhkX9aBwqNnrMvUCgYEA+Lft\r\n"
			+ "fGTJDD9oTulu+W5N9wHyI1P67Sga3l2fD9V5mi0kCm40Mwact7OSo5A9eGHc6jU+\r\n"
			+ "qNCP3AfUs1CuuYA+T4SIpNBXH0v9egwgI3HGR5PQKNJgvTuwS5KGJqmr0VLjtpOs\r\n"
			+ "MvVuIb89FlYv8V67YzrzyZK+TzN+jjCQLo/KudUCgYBG3BGtSRuHrLfO+TEfZWkT\r\n"
			+ "8rHUhBmeQnj76sTKTNssLWPqeAtRk5qP5Qkp00GGvuNxB8byUMfboQziD5sR5He4\r\n"
			+ "OP4EM2FGt+ihJsU1h6C0QW6Fvb5MT/VocW8ckUn3hSaiwCNw+XaUuNwjxGQmvbZr\r\n"
			+ "u/NMbj7EIQvitNgzemLpLQKBgFkimYLOH9s60igNso7DwD84vE166F+b0t1EUh/7\r\n"
			+ "koP1j4gJbL2orfuL6I6jv9PnxqNNB7K6ZAQn7kuBK80hL7ORvOLMI5z8v8/AjrgF\r\n"
			+ "K1k1mRE4LCFFRe4fieJLRl9TRHaHP9xXZuvDPKJHqxDmsWNjmkDwTTHXM5DDyLhX\r\n"
			+ "IgEtAoGAM0QQQRm2Zz3bvNxOYxWekhAebswu8eMbxrobbM2X4pk4EezZ2kmH0vKH\r\n"
			+ "PYrCMWyUkfOTwq8/adkaLZ9OxCpdzDUx53usaz3A9ojw6z54utOxVzWYhERFLbBa\r\n"
			+ "u+BL9d4o7+ZpYNQQYbMGERySA29PLbE04mwiXOjSSzrxeQMmqVw=\r\n"
			+ "-----END RSA PRIVATE KEY-----";
	public static final String RSA_PUBLIC ="-----BEGIN PUBLIC KEY-----\r\n"
			+ "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA9fh6UtrIDe5Fq49M98i4\r\n"
			+ "tZtKED3vmBxxB3Kx+2mvuJ4lu4Wd00LF7XGyAUoK8wpGEdoJC0Fk7aSyCURvbggg\r\n"
			+ "ydZucJGtipDBWSnpoHdKsYQJcz0ItvxWNYENwYLrIPke5BpxWRN6Mc6BN8RWjtAx\r\n"
			+ "xfuR1BXFinHgGt3JDZu4wSmv+9g7SRXnwGsgy7Ji0b2TBGbExyt2fWZtleR6Eb8v\r\n"
			+ "MPu1qtvHItvlQqV6NYti4qg6xezYOet+sREE4WNoM79cv04RNCqbja+6eYJmtJnG\r\n"
			+ "vqMmQM/Q0jMTtCZBROdPNsOuaKRZCyl+kL/BBy3kBdzuxSEp+pAzD9Zvuw8LWNZy\r\n"
			+ "2QIDAQAB\r\n"
			+ "-----END PUBLIC KEY-----";
	

}
