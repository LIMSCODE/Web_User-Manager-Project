<template>
  <div class="hello">
    <h1>Welcome to {{title}}</h1>
    <input type="text" v-model="input1"/>
  </div>
</template>

<script>
export default {
  data() {
    return {
      title: "개발자의 품격",
      input1:"abc"
    };
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
