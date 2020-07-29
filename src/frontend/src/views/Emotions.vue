<template>
  <div>
    <v-container>
      <h1 class="mt-8 guide">지금 당신의</h1>
      <h1 class="guide">기분은 어떤가요?</h1>

      <v-row class="mt-16">
        <v-col style="text-align: right">
          <v-btn text fab x-large dark to="/post">
            <div class="emoji">😊</div>
          </v-btn>
        </v-col>
        <v-col style="text-align: center">
          <v-btn text fab x-large dark to="/post">
            <div class="emoji">😭</div>
          </v-btn>
        </v-col>
        <v-col style="text-align: left">
          <v-btn text fab x-large dark to="/post">
            <div class="emoji">😠</div>
          </v-btn>
        </v-col>
      </v-row>
      <v-row>
        <v-col style="text-align: right">
          <v-btn text fab x-large dark to="/post">
            <div class="emoji">😶</div>
          </v-btn>
        </v-col>
        <v-col style="text-align: center">
          <v-btn text fab x-large dark to="/post">
            <div class="emoji">😶</div>
          </v-btn>
        </v-col>
        <v-col style="text-align: left">
          <v-btn text fab x-large dark to="/post">
            <div class="emoji">😶</div>
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
.guide {
  font-size: 50px;
}
.emoji {
  font-size: 60px;
}
</style>
