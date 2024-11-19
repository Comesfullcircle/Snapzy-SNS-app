package com.comesfullcircle.board.model.post;

public record PostPostRequestBody(String body){

}

/*public class PostPostRequestBody {
    private String body;

    public PostPostRequestBody(String body) {
        this.body = body;
    }

    public PostPostRequestBody() {

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PostPostRequestBody that = (PostPostRequestBody) object;
        return Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(body);
    }
}*/
