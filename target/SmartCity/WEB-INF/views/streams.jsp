<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
    <title>Energy streams - SmartCity360</title>
</head>
<body class="app line-a">
<c:set var="navActive" value="streams"/>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="app-main">

    <section class="glass flex flex-wrap items-center gap-x-4 gap-y-2 rounded-2xl px-5 py-3">
        <span class="badge-letter">A</span>
        <h1 class="text-xl font-semibold tracking-tight">Energy streams</h1>
        <span class="pill">Decorator</span>
        <p class="min-w-[16rem] flex-1 text-sm text-slate-300">
            Wrap the meter data in security layers. Each layer is added on top, and the meter code never changes.
        </p>
    </section>

    <div class="fill grid gap-4 lg:grid-cols-[24rem_minmax(0,1fr)]">

        <%-- LEFT: the form --%>
        <form method="post" action="${pageContext.request.contextPath}/streams"
              class="glass scroll-y flex flex-col rounded-3xl p-5">

            <label for="reading" class="field-label">Meter reading</label>
            <textarea id="reading" name="reading" rows="6" spellcheck="false"
                      class="field field-mono min-h-[8rem] flex-1 resize-none"><c:out value="${reading}"/></textarea>

            <p class="field-label mt-5">Security layers</p>
            <div class="space-y-2.5">
                <label class="tile">
                    <input type="checkbox" name="compress" value="on" ${compress ? 'checked' : ''}>
                    <div class="box"><p class="tile-title">Compression</p><p class="tile-note">Makes the data smaller with GZIP.</p></div>
                    <span class="mark" aria-hidden="true"></span>
                </label>
                <label class="tile">
                    <input type="checkbox" name="encrypt" value="on" ${encrypt ? 'checked' : ''}>
                    <div class="box"><p class="tile-title">Encryption</p><p class="tile-note">Hides the data with AES-GCM.</p></div>
                    <span class="mark" aria-hidden="true"></span>
                </label>
                <label class="tile">
                    <input type="checkbox" name="auth" value="on" ${auth ? 'checked' : ''}>
                    <div class="box"><p class="tile-title">Authentication</p><p class="tile-note">Signs the data with HMAC-SHA256.</p></div>
                    <span class="mark" aria-hidden="true"></span>
                </label>
            </div>
            <p class="mt-2 text-xs text-slate-400">Layers always run in this order: compression, encryption, authentication.</p>

            <button type="submit" class="btn mt-4 self-start">Process stream</button>
        </form>

        <%-- RIGHT: the result --%>
        <section class="glass scroll-y flex flex-col rounded-3xl p-5">
            <c:choose>
                <c:when test="${not empty result}">

                    <h2 class="panel-title">Stream built</h2>
                    <div class="mt-3 flex flex-wrap items-center gap-2">
                        <c:forEach var="part" items="${fn:split(description, ' + ')}" varStatus="st">
                            <span class="pill"><c:out value="${part}"/></span>
                            <c:if test="${not st.last}"><span class="text-xs text-slate-400" aria-hidden="true">wrapped by</span></c:if>
                        </c:forEach>
                    </div>

                    <h2 class="panel-title mt-6">Size</h2>
                    <div class="mt-3 grid gap-4 text-sm sm:grid-cols-2">
                        <div>
                            <div class="flex justify-between text-slate-300"><span>Before</span><span><c:out value="${rawLength}"/> characters</span></div>
                            <div class="mt-1.5 h-2 overflow-hidden rounded-full bg-white/10">
                                <div class="h-full rounded-full bg-slate-300"
                                     style="width: ${rawLength > resultLength ? 100 : rawLength * 100 / resultLength}%"></div>
                            </div>
                        </div>
                        <div>
                            <div class="flex justify-between text-slate-300"><span>After</span><span><c:out value="${resultLength}"/> characters</span></div>
                            <div class="mt-1.5 h-2 overflow-hidden rounded-full bg-white/10">
                                <div class="accent-bg h-full rounded-full"
                                     style="width: ${resultLength > rawLength ? 100 : resultLength * 100 / rawLength}%"></div>
                            </div>
                        </div>
                    </div>

                    <h2 class="panel-title mt-6">Final output sent to the city</h2>
                    <pre class="code mt-2 min-h-[8rem] flex-1"><c:out value="${result}"/></pre>
                </c:when>

                <c:otherwise>
                    <h2 class="panel-title">How the layers wrap the stream</h2>
                    <p class="mt-1 text-sm text-slate-300">Choose your layers on the left, then select Process stream. The result appears here.</p>

                    <div class="ring-box mt-5 max-w-md">
                        <p class="text-xs font-semibold accent-text">Authentication</p>
                        <div class="ring-box mt-2">
                            <p class="text-xs font-semibold accent-text">Encryption</p>
                            <div class="ring-box mt-2">
                                <p class="text-xs font-semibold accent-text">Compression</p>
                                <div class="mt-2 rounded-xl border border-white/20 bg-white/10 px-3 py-2 text-sm font-semibold">Energy meter stream</div>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </div>
</main>
</body>
</html>
