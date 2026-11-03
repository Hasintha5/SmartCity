<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
    <title>SmartCity360</title>
</head>
<body class="app line-home">
<c:set var="navActive" value="home"/>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<%-- Count how many modules are ready --%>
<c:set var="readyCount" value="0"/>
<c:forEach var="m" items="${modules}">
    <c:if test="${m.ready}">
        <c:set var="readyCount" value="${readyCount + 1}"/>
    </c:if>
</c:forEach>

<main class="app-main">
    <div class="fill grid gap-4 lg:grid-cols-[minmax(0,5fr)_minmax(0,8fr)]">

        <%-- LEFT: the welcome panel --%>
        <section class="glass scroll-y flex flex-col rounded-3xl p-6 lg:p-8">
            <h1 class="text-4xl font-semibold leading-tight tracking-tight xl:text-5xl">One city,<br>five systems.</h1>
            <p class="mt-4 max-w-[36ch] text-slate-300">
                Each part of SmartCity360 runs on its own design pattern. Open a card, or use the menu at the top.
            </p>

            <div class="mt-auto pt-8">
                <div class="flex items-baseline justify-between text-sm">
                    <span class="text-slate-300">Modules live</span>
                    <span class="font-semibold"><c:out value="${readyCount}"/> of <c:out value="${modules.size()}"/></span>
                </div>
                <div class="mt-2 flex gap-1.5" role="img" aria-label="${readyCount} of ${modules.size()} modules live">
                    <c:forEach var="m" items="${modules}">
                        <span class="h-2 flex-1 rounded-full ${m.colorClass} ${m.ready ? 'accent-bg' : 'bg-white/10'}"></span>
                    </c:forEach>
                </div>
            </div>
        </section>

        <%-- RIGHT: the module cards --%>
        <section class="scroll-y grid auto-rows-fr gap-4 sm:grid-cols-2 xl:grid-cols-3">
            <c:forEach var="m" items="${modules}">
                <article class="glass module-card flex flex-col rounded-3xl p-5 ${m.colorClass} ${m.ready ? 'is-live' : 'opacity-70'}">
                    <div class="flex items-center justify-between gap-3">
                        <span class="badge-letter"><c:out value="${m.part}"/></span>
                        <span class="pill"><c:out value="${m.pattern}"/></span>
                    </div>

                    <c:choose>
                        <c:when test="${m.ready}">
                            <h2 class="mt-4 text-xl font-semibold">
                                <a href="${pageContext.request.contextPath}${m.path}" class="after:absolute after:inset-0 after:content-['']">
                                    <c:out value="${m.title}"/>
                                </a>
                            </h2>
                        </c:when>
                        <c:otherwise>
                            <h2 class="mt-4 text-xl font-semibold"><c:out value="${m.title}"/></h2>
                        </c:otherwise>
                    </c:choose>

                    <p class="mt-1 flex-1 text-sm text-slate-300"><c:out value="${m.description}"/></p>

                    <p class="mt-4 text-sm font-semibold">
                        <c:choose>
                            <c:when test="${m.ready}">
                                <span class="accent-text inline-flex items-center gap-1">Open module
                                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M9 6l6 6-6 6"/></svg>
                                </span>
                            </c:when>
                            <c:otherwise><span class="text-slate-400">Not built yet</span></c:otherwise>
                        </c:choose>
                    </p>
                </article>
            </c:forEach>

            <article class="glass flex flex-col justify-center rounded-3xl p-5">
                <h2 class="text-base font-semibold">About this build</h2>
                <dl class="mt-3 space-y-2 text-sm">
                    <div><dt class="text-xs text-slate-400">Unit</dt><dd>Object Oriented Design Patterns II</dd></div>
                    <div><dt class="text-xs text-slate-400">Platform</dt><dd>Jakarta EE 10 on Payara 6</dd></div>
                    <div><dt class="text-xs text-slate-400">Data</dt><dd>Kept in memory, no database</dd></div>
                </dl>
            </article>
        </section>
    </div>
</main>
</body>
</html>
