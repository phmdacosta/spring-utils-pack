package com.pedrocosta.utils.mailsender;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Email implements Serializable {
    private String from;
    private Set<String> to;
    private String subject;
    private String body;

    public Email() {
        this.to = new HashSet<>();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Set<String> getTo() {
        return to;
    }

    public void setTo(Set<String> to) {
        this.to = to;
    }

    public void addTo(String ... to) {
        this.getTo().addAll(Arrays.asList(to));
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return Objects.equals(getFrom(), email.getFrom()) && Objects.equals(getTo(), email.getTo()) && Objects.equals(getSubject(), email.getSubject()) && Objects.equals(getBody(), email.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getTo(), getSubject(), getBody());
    }

    @Override
    public String toString() {
        return "Email{" +
                "from='" + from + '\'' +
                ", to=" + to +
                ", subject='" + subject + '\'' +
                '}';
    }
}
