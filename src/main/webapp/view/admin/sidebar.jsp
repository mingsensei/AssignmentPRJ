<ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">
  <!-- Sidebar - Brand -->
  <a class="sidebar-brand d-flex align-items-center justify-content-center" href="${pageContext.request.contextPath}/admin/dashboard.jsp">
    <div class="sidebar-brand-icon rotate-n-15">
      <i class="fas fa-tachometer-alt"></i>
    </div>
    <div class="sidebar-brand-text mx-3">Admin Panel</div>
  </a>

  <hr class="sidebar-divider my-0">

  <li class="nav-item ${pageContext.request.servletPath.contains('/admin/dashboard') ? 'active' : ''}">
    <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">
      <i class="fas fa-fw fa-tachometer-alt"></i>
      <span>Dashboard</span>
    </a>
  </li>

  <hr class="sidebar-divider">

  <li class="nav-item ${pageContext.request.servletPath.contains('/admin/setting') ? 'active' : ''}">
    <a class="nav-link" href="${pageContext.request.contextPath}/admin/settings">
      <i class="fas fa-fw fa-cog"></i>
      <span>Settings</span>
    </a>
  </li>

  <li class="nav-item ${pageContext.request.servletPath.contains('/admin/user') ? 'active' : ''}">
    <a class="nav-link" href="${pageContext.request.contextPath}/admin/user">
      <i class="fas fa-fw fa-user"></i>
      <span>User</span>
    </a>
  </li>

  <li class="nav-item ${pageContext.request.servletPath.contains('/admin/order') ? 'active' : ''}">
    <a class="nav-link" href="${pageContext.request.contextPath}/admin/order">
      <i class="fas fa-fw fa-credit-card"></i>
      <span>Order</span>
    </a>
  </li>

  <li class="nav-item ${pageContext.request.servletPath.contains('/admin/role') ? 'active' : ''}">
    <a class="nav-link" href="${pageContext.request.contextPath}/admin/role">
      <i class="fas fa-fw fa-user-tag"></i>
      <span>Role</span>
    </a>
  </li>

  <li class="nav-item ${pageContext.request.servletPath.contains('/admin/course') ? 'active' : ''}">
    <a class="nav-link" href="${pageContext.request.contextPath}/admin/course">
      <i class="fas fa-fw fa-book"></i>
      <span>Course</span>
    </a>
  </li>

  <li class="nav-item ${pageContext.request.servletPath.contains('/admin/slider') ? 'active' : ''}">
    <a class="nav-link" href="${pageContext.request.contextPath}/admin/slider">
      <i class="fas fa-fw fa-images"></i>
      <span>Slider</span>
    </a>
  </li>

  <li class="nav-item ${pageContext.request.servletPath.contains('/admin/exam') ? 'active' : ''}">
    <a class="nav-link" href="${pageContext.request.contextPath}/admin/exam">
      <i class="fas fa-fw fa-clipboard-list"></i>
      <span>Exam</span>
    </a>
  </li>

  <li class="nav-item ${pageContext.request.servletPath.contains('/admin/question') ? 'active' : ''}">
    <a class="nav-link" href="${pageContext.request.contextPath}/admin/question">
      <i class="fas fa-fw fa-question-circle"></i>
      <span>Question</span>
    </a>
  </li>

  <li class="nav-item ${pageContext.request.servletPath.contains('/admin/blog') ? 'active' : ''}">
    <a class="nav-link" href="${pageContext.request.contextPath}/admin/post">
      <i class="fas fa-fw fa-newspaper"></i>
      <span>Blog</span>
    </a>
  </li>

  <hr class="sidebar-divider d-none d-md-block">

  <div class="text-center d-none d-md-inline">
    <button class="rounded-circle border-0" id="sidebarToggle"></button>
  </div>
</ul>
