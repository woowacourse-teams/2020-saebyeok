<template>
  <div>
    <v-container>
      <div class="mt-8 text-h4">
        지금 당신의
        <p>기분은 어떤가요?</p>
      </div>
      <v-row class="mt-16">
        <v-col style="text-align: right">
          <v-btn text fab x-large dark to="/post">
            <div class="text-h2">😊</div>
          </v-btn>
        </v-col>
        <v-col style="text-align: center">
          <v-btn text fab x-large dark to="/post">
            <div class="text-h2">😭</div>
          </v-btn>
        </v-col>
        <v-col style="text-align: left">
          <v-btn text fab x-large dark to="/post">
            <div class="text-h2">😠</div>
          </v-btn>
        </v-col>
      </v-row>
      <v-row>
        <v-col style="text-align: right">
          <v-btn text fab x-large dark to="/post">
            <div class="text-h2">😶</div>
          </v-btn>
        </v-col>
        <v-col style="text-align: center">
          <v-btn text fab x-large dark to="/post">
            <div class="text-h2">😶</div>
          </v-btn>
        </v-col>
        <v-col style="text-align: left">
          <v-btn text fab x-large dark to="/post">
            <div class="text-h2">😶</div>
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {};
</script>

<style scoped></style>
