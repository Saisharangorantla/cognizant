import React from "react";
import Post from "./Post";

class Posts extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            posts: [],
            loading: true,
            error: null
        };
    }

    loadPosts() {
        fetch("https://jsonplaceholder.typicode.com/posts")
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to fetch posts");
                }
                return response.json();
            })
            .then((data) => {
                const posts = data.map(
                    (item) => new Post(item.id, item.title, item.body)
                );

                this.setState({
                    posts: posts,
                    loading: false
                });
            })
            .catch((error) => {
                this.setState({
                    error: error.message,
                    loading: false
                });
            });
    }

    componentDidMount() {
        this.loadPosts();
    }

    componentDidCatch(error, info) {
        console.log("Error:", error);
        console.log("Info:", info);

        this.setState({
            error: error.message
        });
    }

    render() {
        const { posts, loading, error } = this.state;

        if (loading) {
            return <h2>Loading...</h2>;
        }

        if (error) {
            return <h2>Error: {error}</h2>;
        }

        return (
            <div>
                <h1>Posts</h1>

                {posts.map((post) => (
                    <div
                        key={post.id}
                        style={{
                            border: "1px solid #ccc",
                            margin: "10px",
                            padding: "10px",
                            borderRadius: "5px"
                        }}
                    >
                        <h3>{post.title}</h3>
                        <p>{post.body}</p>
                    </div>
                ))}
            </div>
        );
    }
}

export default Posts;