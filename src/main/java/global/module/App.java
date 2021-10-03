package global.module;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * 애플리케이션 클래스
 *
 * @author RWB
 * @since 2021.09.29 Wed 22:40:20
 */
@ApplicationPath("/api")
public class App extends Application
{
    // api 접두사 요청을 jersey가 담당
}