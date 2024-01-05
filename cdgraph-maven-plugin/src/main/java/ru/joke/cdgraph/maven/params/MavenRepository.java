package ru.joke.cdgraph.maven.params;

import org.apache.maven.plugins.annotations.Parameter;

public final class MavenRepository {

    @Parameter(required = true)
    private String url;
    private String login;
    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "MavenRepository{" + "url='" + url + '\'' + ", login='" + login + '\'' + '}';
    }
}
