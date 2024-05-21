<template>
<form class="row g-3 mx-5 pt-5" style="max-width: 700px;">
  <div class="col-6">
    <label for="inputEmail4" class="form-label">Email</label>
    <input type="email" class="form-control" id="inputEmail4" readonly>
  </div>
  <div class="col-4">
    <label for="inputBsn" class="form-label">BSN</label>
    <input type="number" class="form-control" id="inputBsn" readonly>
  </div>
  <div class="col-4">
    <label for="inputFirstName" class="form-label">First Name</label>
    <input type="text" class="form-control" id="inputFirstName" readonly>
  </div>
  <div class="col-4">
    <label for="inputLastName" class="form-label">Last Name</label>
    <input type="text" class="form-control" id="inputLastName" readonly>
  </div>
  <div class="col-md-4">
    <label for="inputPhone" class="form-label">Phone Number</label>
    <input type="number" class="form-control" id="inputPhone" readonly>
  </div>
  <div class="col-md-4">
    <label for="inputPhone" class="form-label">Absolut Limit</label>
    <input type="number" class="form-control" id="inputPhone">
  </div><div class="col-md-4">
    <label for="inputPhone" class="form-label">Daily Limit</label>
    <input type="number" class="form-control" id="inputPhone">
  </div>
  <div class="col-12">
    <a class="nav-link p-2" style="font-size: 18px; cursor: pointer; width: 150px; height: 50px; float: right; text-align: center;">Open Account</a>
  </div>
</form>
</template>

<script>
export default {
    name: 'RegistrationOverview'
}
</script>

<style>
label{
  color: white;
}
</style>