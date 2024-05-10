<template>
    <form>
            <div class="form-floating py-2">
                <input type="text" class="form-control" id="floatingInput" placeholder="username" v-model="username">
                <label for="floatingInput">username</label>
            </div>
            <div class="form-floating">
                <input type="password" class="form-control" id="floatingPassword" placeholder="Password"  v-model="password">
                <label for="floatingPassword">password</label>
            </div>

            <div class="container d-flex justify-content-center flex-nowrap">
                <a class="nav-link py-4 px-3" @click="login()" style="font-size: 27px; cursor: pointer;">login</a>
                <router-link class="nav-link py-4 px-3" to="/register" style="font-size: 27px">sign up</router-link>
            </div>
        </form>
</template>
<script>

</script>